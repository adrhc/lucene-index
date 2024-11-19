package ro.go.adrhc.persistence.lucene.core.bare.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Bits;
import org.springframework.util.Assert;
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

	public boolean isEmpty() throws IOException {
		if (indexReader == null) {
			return true;
		} else {
			return count() == 0;
		}
	}

	public int count() throws IOException {
		if (indexReader == null) {
			return 0;
		}
		IndexSearcher searcher = new IndexSearcher(indexReader);
		return searcher.count(new MatchAllDocsQuery());
	}

	public int count(Query query) throws IOException {
		if (indexReader == null) {
			return 0;
		}
		IndexSearcher searcher = new IndexSearcher(indexReader);
		return searcher.count(query);
	}

	public Stream<Document> getDocumentStream() {
		return getDocProjectionStream(Set.of());
	}

	public Stream<Document> getDocProjectionStream(Set<String> fieldNames) {
		if (indexReader == null) {
			return Stream.empty();
		}
		// liveDocs can be null if the reader has no deletions
		Bits liveDocs = MultiBits.getLiveDocs(indexReader);
		return storedFields()
				.map(storedFields -> doGetAll(liveDocs, fieldNames, storedFields))
				.orElseGet(Stream::of);
	}

	public Stream<IndexableField> getFields(String fieldName) {
		return getDocProjectionStream(Set.of(fieldName)).map(doc -> doc.getField(fieldName));
	}

	public Stream<Object> findFieldValues(
			String fieldName, Query query, int numHits) throws IOException {
		if (indexReader == null) {
			return Stream.empty();
		}
		StoredFields storedFields = indexReader.storedFields();
		TopDocs topDocs = useIndexSearcher(s -> s.search(query, numHits));
		OneStoredObjectFieldVisitor fieldVisitor =
				new OneStoredObjectFieldVisitor(fieldName);
		return Arrays.stream(topDocs.scoreDocs)
				.map(scoreDoc -> safelyGetOneFieldValue(storedFields, fieldVisitor, scoreDoc))
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

	protected Stream<ScoreDocAndDocument> doFindMany(
			SneakyFunction<IndexSearcher, TopDocs, IOException> topDocsSupplier)
			throws IOException {
		if (indexReader == null) {
			return Stream.empty();
		}
		StoredFields storedFields = indexReader.storedFields();
		TopDocs topDocs = useIndexSearcher(topDocsSupplier);
		return Arrays.stream(topDocs.scoreDocs)
				.map(scoreDoc -> safelyGetScoreAndDocument(storedFields, scoreDoc))
				.flatMap(Optional::stream);
	}

	protected Stream<Document> doGetAll(Bits liveDocs,
			Set<String> fieldNames, StoredFields storedFields) {
		Assert.isTrue(indexReader != null, "indexReader must be not null!");
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
		Assert.isTrue(indexReader != null, "indexReader must be not null!");
		try {
			return Optional.of(indexReader.storedFields());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return Optional.empty();
	}

	@Override
	public void close() throws IOException {
		if (indexReader != null) {
			indexReaderPool.dismissReader(indexReader);
		}
	}

	protected <R> R useIndexSearcher(
			SneakyFunction<IndexSearcher, R, IOException> topDocsSupplier)
			throws IOException {
		Assert.isTrue(indexReader != null, "indexReader must be not null!");
		return topDocsSupplier.apply(new IndexSearcher(indexReader));
	}

	private <V> V safelyGetOneFieldValue(StoredFields storedFields,
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
