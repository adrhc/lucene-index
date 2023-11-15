package ro.go.adrhc.persistence.lucene.core.read;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.MultiBits;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Bits;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class DocumentsIndexReader implements Closeable {
	private final IndexReaderPool indexReaderPool;
	private final IndexReader indexReader;
	private final int numHits;

	public static DocumentsIndexReader create(DocumentsIndexReaderParams params) throws IOException {
		return new DocumentsIndexReader(params.getIndexReaderPool(),
				params.getIndexReaderPool().get(), params.getNumHits());
	}

	public static DocumentsIndexReader create(int numHits, IndexReaderPool indexReaderPool) throws IOException {
		return new DocumentsIndexReader(indexReaderPool, indexReaderPool.get(), numHits);
	}

	public Stream<Document> getAll() {
		return getFieldsOfAll(Set.of());
	}

	public Stream<IndexableField> getFieldOfAll(String fieldName) {
		return getFieldsOfAll(Set.of(fieldName)).map(doc -> doc.getField(fieldName));
	}

	public Stream<Document> getFieldsOfAll(Set<String> fieldNames) {
		// liveDocs can be null if the reader has no deletions
		Bits liveDocs = MultiBits.getLiveDocs(indexReader);
		return storedFields()
				.map(storedFields -> doGetAll(liveDocs, storedFields, fieldNames))
				.orElseGet(Stream::of);
	}

	public Stream<ScoreAndDocument> findMany(Query query) throws IOException {
		TopDocsStoredFields topDocsStoredFields = topDocsStoredFields(query);
		return Stream.of(topDocsStoredFields.topDocs().scoreDocs)
				.map(scoreDoc -> safelyGetScoreAndDocument(topDocsStoredFields, scoreDoc))
				.flatMap(Optional::stream);
	}

	public Optional<Document> findFirst(Query query) throws IOException {
		TopDocsStoredFields topDocsStoredFields = topDocsStoredFields(query);
		if (topDocsStoredFields.topDocs().totalHits.value > 0) {
			return Optional.of(topDocsStoredFields.storedFields()
					.document(topDocsStoredFields.topDocs().scoreDocs[0].doc));
		} else {
			return Optional.empty();
		}
	}

	public int count(Query query) throws IOException {
		IndexSearcher searcher = new IndexSearcher(indexReader);
		return searcher.count(query);
	}

	public int count() throws IOException {
		IndexSearcher searcher = new IndexSearcher(indexReader);
		return searcher.count(new MatchAllDocsQuery());
	}

	protected Optional<ScoreAndDocument> safelyGetScoreAndDocument(
			TopDocsStoredFields topDocsStoredFields, ScoreDoc scoreDoc) {
		return safelyGetDocument(topDocsStoredFields.storedFields(), scoreDoc.doc)
				.map(doc -> new ScoreAndDocument(scoreDoc.score, doc));
	}

	protected Optional<Document> safelyGetDocument(StoredFields storedFields, int docIndex) {
		return safelyGetDocument(storedFields, Set.of(), docIndex);
	}

	protected Stream<Document> doGetAll(Bits liveDocs, StoredFields storedFields, Set<String> fieldNames) {
		return IntStream.range(0, indexReader.maxDoc())
				.filter(docIndex -> liveDocs == null || liveDocs.get(docIndex))
				.mapToObj(docIndex -> this.safelyGetDocument(storedFields, fieldNames, docIndex))
				.flatMap(Optional::stream);
	}

	/**
	 * indexReader.document might fail if the document
	 * is meanwhile purged (not only marked as removed)
	 */
	protected Optional<Document> safelyGetDocument(StoredFields storedFields, Set<String> fieldNames, int docIndex) {
		try {
			if (fieldNames.isEmpty()) {
				return Optional.of(storedFields.document(docIndex));
			} else {
				return Optional.of(storedFields.document(docIndex, fieldNames));
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return Optional.empty();
	}

	protected Optional<StoredFields> storedFields() {
		try {
			return Optional.of(indexReader.storedFields());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return Optional.empty();
	}

	protected TopDocsStoredFields topDocsStoredFields(Query query) throws IOException {
		IndexSearcher searcher = new IndexSearcher(indexReader);
		TopDocs topDocs = searcher.search(query, numHits);
		return new TopDocsStoredFields(topDocs, searcher.storedFields());
	}

	@Override
	public void close() throws IOException {
		indexReaderPool.returnReader(indexReader);
	}

	private record TopDocsStoredFields(TopDocs topDocs, StoredFields storedFields) {
	}
}
