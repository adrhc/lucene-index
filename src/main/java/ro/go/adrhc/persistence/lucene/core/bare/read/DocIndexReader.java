package ro.go.adrhc.persistence.lucene.core.bare.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Bits;
import org.springframework.lang.Nullable;
import ro.go.adrhc.persistence.lucene.core.bare.read.storedfieldvisitor.StoredObjectFieldValuesVisitor;
import ro.go.adrhc.persistence.lucene.lib.IndexSearcherAccessors;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class DocIndexReader implements Closeable {
	private final IndexReaderPool indexReaderPool;
	private final IndexReader indexReader;

	public static DocIndexReader create(IndexReaderPool indexReaderPool) throws IOException {
		return new DocIndexReader(indexReaderPool, indexReaderPool.getReader());
	}

	public boolean isEmpty() throws IOException {
		return count() == 0;
	}

	public int count() throws IOException {
		IndexSearcher searcher = new IndexSearcher(indexReader);
		return searcher.count(new MatchAllDocsQuery());
	}

	public int count(Query query) throws IOException {
		IndexSearcher searcher = new IndexSearcher(indexReader);
		return searcher.count(query);
	}

	public Stream<Document> getDocumentStream() throws IOException {
		return getDocProjectionStream(Set.of());
	}

	public Stream<Document> getDocProjectionStream(Set<String> fieldNames) throws IOException {
		// liveDocs can be null if the reader has no deletions
		Bits liveDocs = MultiBits.getLiveDocs(indexReader);
		return storedFields()
			.map(storedFields -> doGetAll(liveDocs, fieldNames, storedFields))
			.orElseGet(Stream::of);
	}

	/**
	 * Returns all fieldName field values!
	 */
	public Stream<IndexableField> getFields(String fieldName) throws IOException {
		return getDocProjectionStream(Set.of(fieldName))
			.mapMulti((doc, sink) -> {
				for (IndexableField field : doc.getFields(fieldName)) {
					sink.accept(field);
				}
			});
	}

	/**
	 * Returns all fieldName field values across documents!
	 */
	public Stream<Object> findFieldValues(
		String fieldName, Query query, int numHits) throws IOException {
		return findFieldValues(fieldName, query, numHits, null);
	}

	public Stream<Object> findFieldValues(
		String fieldName, Query query, int numHits, @Nullable Sort sort) throws IOException {
		StoredFields storedFields = indexReader.storedFields();
		TopDocs topDocs = useIndexSearcher(s -> IndexSearcherAccessors.search(s, query, numHits, sort));
		StoredObjectFieldValuesVisitor fieldVisitor = new StoredObjectFieldValuesVisitor(fieldName);
		return Arrays.stream(topDocs.scoreDocs)
			.flatMap(scoreDoc -> safelyGetFieldValues(storedFields, fieldVisitor, scoreDoc).stream())
			.filter(Objects::nonNull);
	}

	/**
	 * @return limited by numHits
	 */
	public Stream<ScoreDocAndDocument> findMany(Query query, int numHits) throws IOException {
		return doFindMany(s -> s.search(query, numHits));
	}

	public Stream<ScoreDocAndDocument> findMany(
		Query query, int numHits, Sort sort) throws IOException {
		return doFindMany(s -> s.search(query, numHits, sort));
	}

	public Stream<ScoreDocAndDocument> findManyAfter(ScoreDoc after,
		Query query, int numHits, Sort sort) throws IOException {
		return doFindMany(s -> s.searchAfter(after, query, numHits, sort));
	}

	@Override
	public void close() throws IOException {
		if (indexReader != null) {
			indexReaderPool.dismissReader(indexReader);
		}
	}

	protected Stream<ScoreDocAndDocument> doFindMany(
		SneakyFunction<IndexSearcher, TopDocs, IOException> topDocsSupplier) throws IOException {
		StoredFields storedFields = indexReader.storedFields();
		TopDocs topDocs = useIndexSearcher(topDocsSupplier);
		return Arrays.stream(topDocs.scoreDocs)
			.map(scoreDoc -> safelyGetScoreAndDocument(storedFields, scoreDoc))
			.flatMap(Optional::stream);
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

	protected Optional<StoredFields> storedFields() throws IOException {
		return Optional.of(indexReader.storedFields());
	}

	protected <R> R useIndexSearcher(
		SneakyFunction<IndexSearcher, R, IOException> topDocsSupplier) throws IOException {
		return topDocsSupplier.apply(new IndexSearcher(indexReader));
	}

	private List<Object> safelyGetFieldValues(StoredFields storedFields,
		StoredObjectFieldValuesVisitor fieldVisitor, ScoreDoc scoreDoc) {
		fieldVisitor.reset();
		safelyVisitDocument(storedFields, fieldVisitor, scoreDoc);
		return fieldVisitor.getValues();
	}

	/**
	 * If a document have multiple values for the stored field "name" will
	 * fieldVisitor (suppose it accepts "name") receive all "name" values of a document?
	 * <p>
	 * Yes. StoredFields.document(doc, fieldVisitor) will call the visitor for each stored
	 * occurrence of "name" in that document, so it receives all values the visitor accepts.
	 */
	private void safelyVisitDocument(StoredFields storedFields,
		StoredFieldVisitor fieldVisitor, ScoreDoc scoreDoc) {
		try {
			storedFields.document(scoreDoc.doc, fieldVisitor);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
