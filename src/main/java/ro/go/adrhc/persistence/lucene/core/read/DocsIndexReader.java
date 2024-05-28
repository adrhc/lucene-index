package ro.go.adrhc.persistence.lucene.core.read;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Bits;
import ro.go.adrhc.persistence.lucene.core.read.storedfieldvisitor.AbstractOneStoredFieldVisitor;
import ro.go.adrhc.persistence.lucene.core.read.storedfieldvisitor.OneStoredObjectFieldVisitor;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class DocsIndexReader implements Closeable {
	private final IndexReaderPool indexReaderPool;
	private final IndexReader indexReader;
	private final int numHits;

	public static DocsIndexReader create(DocsIndexReaderParams params) throws IOException {
		return new DocsIndexReader(params.getIndexReaderPool(),
				params.getIndexReaderPool().getReader(), params.getNumHits());
	}

	public static DocsIndexReader create(int numHits, IndexReaderPool indexReaderPool) throws IOException {
		return new DocsIndexReader(indexReaderPool, indexReaderPool.getReader(), numHits);
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

	/**
	 * @return limited by numHits
	 */
	public Stream<ScoreAndDocument> findMany(Query query) throws IOException {
		TopDocsStoredFields topDocsStoredFields = topDocsStoredFields(query);
		return topDocsStoredFields
				.rawMap(scoreDoc -> safelyGetScoreAndDocument(topDocsStoredFields, scoreDoc))
				.flatMap(Optional::stream);
	}

	/**
	 * @return limited by numHits
	 */
	public Stream<Object> findFieldValues(String fieldName, Query query) throws IOException {
		TopDocsStoredFields topDocsStoredFields = topDocsStoredFields(query);
		OneStoredObjectFieldVisitor fieldVisitor =
				new OneStoredObjectFieldVisitor(fieldName);
		return topDocsStoredFields
				.rawMap(scoreDoc -> safelyGetFieldValue(fieldVisitor, topDocsStoredFields, scoreDoc))
				.filter(Objects::nonNull);
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

	protected Stream<Document> doGetAll(Bits liveDocs, StoredFields storedFields, Set<String> fieldNames) {
		return IntStream.range(0, indexReader.maxDoc())
				.filter(docIndex -> liveDocs == null || liveDocs.get(docIndex))
				.mapToObj(docIndex -> this.safelyGetDocument(storedFields, fieldNames, docIndex))
				.flatMap(Optional::stream);
	}

	protected Optional<Document> safelyGetDocument(StoredFields storedFields, int docIndex) {
		return safelyGetDocument(storedFields, Set.of(), docIndex);
	}

	/**
	 * indexReader.document might fail if the document
	 * is meanwhile purged (not only marked as removed)
	 */
	protected Optional<Document> safelyGetDocument(
			StoredFields storedFields, Set<String> fieldNames, int docIndex) {
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

	private <V> V safelyGetFieldValue(AbstractOneStoredFieldVisitor<V> fieldVisitor,
			TopDocsStoredFields topDocsStoredFields, ScoreDoc scoreDoc) {
		fieldVisitor.reset();
		safelyVisitDocument(fieldVisitor, topDocsStoredFields, scoreDoc);
		return fieldVisitor.getValue();
	}

	private void safelyVisitDocument(StoredFieldVisitor fieldVisitor,
			TopDocsStoredFields topDocsStoredFields, ScoreDoc scoreDoc) {
		try {
			topDocsStoredFields.document(scoreDoc.doc, fieldVisitor);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

}
