package ro.go.adrhc.persistence.lucene.core.bare.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Bits;
import ro.go.adrhc.persistence.lucene.core.bare.read.storedfieldvisitor.AbstractOneStoredFieldVisitor;
import ro.go.adrhc.persistence.lucene.core.bare.read.storedfieldvisitor.OneStoredObjectFieldVisitor;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class DocsIndexReader implements Closeable {
	private final IndexReaderPool indexReaderPool;
	private final IndexReader indexReader;

	public static DocsIndexReader create(IndexReaderPool indexReaderPool) throws IOException {
		return new DocsIndexReader(indexReaderPool, indexReaderPool.getReader());
	}

	public Stream<Document> getDocumentStream() {
		return getDocProjectionStream(Set.of());
	}

	public Stream<IndexableField> getFieldStream(String fieldName) {
		return getDocProjectionStream(Set.of(fieldName)).map(doc -> doc.getField(fieldName));
	}

	public Stream<Document> getDocProjectionStream(Set<String> fieldNames) {
		// liveDocs can be null if the reader has no deletions
		Bits liveDocs = MultiBits.getLiveDocs(indexReader);
		return storedFields()
				.map(storedFields -> doGetAll(liveDocs, fieldNames, storedFields))
				.orElseGet(Stream::of);
	}

	/**
	 * @return limited by numHits
	 */
	public Stream<ScoreDocAndDocument> findMany(Query query, int numHits) throws IOException {
		StoredFields storedFields = indexReader.storedFields();
		TopDocs topDocs = useIndexSearcher(s -> s.search(query, numHits));
		return Arrays.stream(topDocs.scoreDocs)
				.map(scoreDoc -> safelyGetScoreAndDocument(storedFields, scoreDoc))
				.flatMap(Optional::stream);
	}

	public Stream<ScoreDocAndDocument> findMany(
			Query query, int numHits, Sort sort) throws IOException {
		StoredFields storedFields = indexReader.storedFields();
		TopDocs topDocs = useIndexSearcher(s -> s.search(query, numHits, sort));
		return Arrays.stream(topDocs.scoreDocs)
				.map(scoreDoc -> safelyGetScoreAndDocument(storedFields, scoreDoc))
				.flatMap(Optional::stream);
	}

	public Stream<ScoreDocAndDocument> findManyAfter(ScoreDoc after,
			Query query, int numHits, Sort sort) throws IOException {
		StoredFields storedFields = indexReader.storedFields();
		TopDocs topDocs = useIndexSearcher(s -> s.searchAfter(after, query, numHits, sort));
		return Arrays.stream(topDocs.scoreDocs)
				.map(scoreDoc -> safelyGetScoreAndDocument(storedFields, scoreDoc))
				.flatMap(Optional::stream);
	}

	public Stream<Object> findFieldValues(
			String fieldName, Query query, int numHits) throws IOException {
		StoredFields storedFields = indexReader.storedFields();
		TopDocs topDocs = useIndexSearcher(s -> s.search(query, numHits));
		OneStoredObjectFieldVisitor fieldVisitor =
				new OneStoredObjectFieldVisitor(fieldName);
		return Arrays.stream(topDocs.scoreDocs)
				.map(scoreDoc -> safelyVisitOneFieldDocument(storedFields, fieldVisitor, scoreDoc))
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

	protected Stream<Document> doGetAll(Bits liveDocs,
			Set<String> fieldNames, StoredFields storedFields) {
		return IntStream.range(0, indexReader.maxDoc())
				.filter(docIndex -> liveDocs == null || liveDocs.get(docIndex))
				.mapToObj(docIndex -> this.safelyGetDocument(
						storedFields, fieldNames, docIndex))
				.flatMap(Optional::stream);
	}

	protected Optional<ScoreDocAndDocument> safelyGetScoreAndDocument(
			StoredFields storedFields, ScoreDoc scoreDoc) {
		return safelyGetDocument(storedFields, null, scoreDoc.doc)
				.map(doc -> new ScoreDocAndDocument(scoreDoc, doc));
	}

	/**
	 * indexReader.document might fail if the document
	 * is meanwhile purged (not only marked as removed)
	 */
	protected Optional<Document> safelyGetDocument(
			StoredFields storedFields, Set<String> fieldNames, int docIndex) {
		try {
			if (fieldNames == null || fieldNames.isEmpty()) {
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

	protected <R> R useIndexSearcher(
			SneakyFunction<IndexSearcher, R, IOException> topDocsSupplier)
			throws IOException {
		return topDocsSupplier.apply(new IndexSearcher(indexReader));
	}

	@Override
	public void close() throws IOException {
		indexReaderPool.dismissReader(indexReader);
	}

	private <V> V safelyVisitOneFieldDocument(StoredFields storedFields,
			AbstractOneStoredFieldVisitor<V> fieldVisitor, ScoreDoc scoreDoc) {
		fieldVisitor.reset();
		safelyVisitDocument(storedFields, fieldVisitor, scoreDoc);
		return fieldVisitor.getValue();
	}

	private void safelyVisitDocument(StoredFields storedFields,
			StoredFieldVisitor fieldVisitor, ScoreDoc scoreDoc) {
		try {
			storedFields.document(scoreDoc.doc, fieldVisitor);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

}
