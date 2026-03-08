package ro.go.adrhc.persistence.lucene.core.bare.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;
import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Bits;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ro.go.adrhc.persistence.lucene.lib.IndexSearcherAccessors;
import ro.go.adrhc.util.fn.BiFunctionUtils;
import ro.go.adrhc.util.fn.TriFunctionUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class DocIndexReader implements Closeable {
	private final SneakyConsumer<IndexReader, IOException> closeStrategy;
	private final IndexReader indexReader;

	public boolean isEmpty() throws IOException {
		return count() == 0;
	}

	public int count() throws IOException {
		IndexSearcher searcher = new IndexSearcher(indexReader);
		return searcher.count(new MatchAllDocsQuery());
	}

	public int countByQuery(Query query) throws IOException {
		IndexSearcher searcher = new IndexSearcher(indexReader);
		return searcher.count(query);
	}

	/**
	 * @param fieldName might be multiple times in a document and all its occurrences will be returned
	 * @return all fieldName fields
	 */
	public Stream<IndexableField> getFieldValues(String fieldName) throws IOException {
		return getProjections(Set.of(fieldName))
			.mapMulti((doc, sink) -> {
				for (IndexableField field : doc.getFields(fieldName)) {
					sink.accept(field);
				}
			});
	}

	public Stream<Document> getDocuments() throws IOException {
		return getProjections(Set.of());
	}

	/**
	 * @return documents with only fieldNames fields (if fieldNames is empty, all fields are returned)
	 */
	public Stream<Document> getProjections(Set<String> fieldNames) throws IOException {
		// liveDocs can be null if the reader has no deletions
		Bits liveDocs = MultiBits.getLiveDocs(indexReader);
		StoredFields storedFields = indexReader.storedFields();
		return IntStream.range(0, indexReader.maxDoc())
			.filter(i -> liveDocs == null || liveDocs.get(i))
			.mapToObj(i -> TriFunctionUtils.failToEmpty(
				LuceneDocumentFactory::of, storedFields, fieldNames, i))
			.flatMap(Optional::stream);
	}

	/**
	 * @param numHits is used by Lucene to limit the number of documents to return
	 * @return all fieldName field values across documents
	 */
	public Stream<Object> findFieldValues(
		String fieldName, Query query, int numHits) throws IOException {
		return findFieldValuesSorted(fieldName, query, numHits, null);
	}

	/**
	 * @param numHits is used by Lucene to limit the number of documents to return
	 * @param sort    it is used by Lucene to sort the documents before retrieving the field values
	 * @return all fieldName field values across documents sorted by sort
	 */
	public Stream<Object> findFieldValuesSorted(
		String fieldName, Query query, int numHits, @Nullable Sort sort) throws IOException {
		StoredFields storedFields = indexReader.storedFields();
		TopDocs topDocs = executeSearch(s -> IndexSearcherAccessors.search(s, query, numHits, sort));
		StoredObjectFieldValuesVisitor fieldVisitor = new StoredObjectFieldValuesVisitor(fieldName);
		return Arrays.stream(topDocs.scoreDocs)
			.mapMulti((scoreDoc, sink) ->
				safelyGetFieldValues(storedFields, fieldVisitor, scoreDoc).forEach(sink)
			);
	}

	/**
	 * @param numHits is used by Lucene to limit the number of documents to return
	 */
	public Stream<ScoreAndDocument> findMany(Query query, int numHits) throws IOException {
		return doFindMany(s -> s.search(query, numHits));
	}

	/**
	 * @param numHits is used by Lucene to limit the number of documents to return
	 */
	public Stream<ScoreAndDocument> findManySorted(
		Query query, int numHits, Sort sort) throws IOException {
		return doFindMany(s -> s.search(query, numHits, sort));
	}

	/**
	 * @param numHits is used by Lucene to limit the number of documents to return
	 */
	public Stream<ScoreAndDocument> findManyAfter(ScoreDoc after,
		Query query, int numHits, Sort sort) throws IOException {
		return doFindMany(s -> s.searchAfter(after, query, numHits, sort));
	}

	/**
	 * @return whether there are more results after the given scoreDoc for the given sort
	 */
	public boolean hasAfter(ScoreDoc scoreDoc, Sort sort) throws IOException {
		return new IndexSearcher(indexReader)
			.searchAfter(scoreDoc, new MatchAllDocsQuery(), 1, sort)
			.scoreDocs.length > 0;
	}

	@Override
	public void close() throws IOException {
		closeStrategy.accept(indexReader);
	}

	protected Stream<ScoreAndDocument> doFindMany(
		SneakyFunction<IndexSearcher, TopDocs, IOException> searchStrategy) throws IOException {
		StoredFields storedFields = indexReader.storedFields();
		TopDocs topDocs = executeSearch(searchStrategy);
		return Arrays.stream(topDocs.scoreDocs)
			.map(scoreDoc -> BiFunctionUtils.failToEmpty(
				ScoreAndDocument::of, storedFields, scoreDoc))
			.flatMap(Optional::stream);
	}

	protected <R> R executeSearch(
		SneakyFunction<IndexSearcher, R, IOException> searchStrategy) throws IOException {
		return searchStrategy.apply(new IndexSearcher(indexReader));
	}

	@NonNull
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
