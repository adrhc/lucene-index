package ro.go.adrhc.persistence.lucene.index.core.read;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiBits;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Bits;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class DocumentIndexReader implements AutoCloseable {
	private final Directory directory;
	private final IndexReader indexReader;
	private final int numHits;

	public static DocumentIndexReader of(int maxResultsPerSearchedSong, Path indexPath) throws IOException {
		Directory directory = FSDirectory.open(indexPath);
		IndexReader indexReader = DirectoryReader.open(directory);
		return new DocumentIndexReader(directory, indexReader, maxResultsPerSearchedSong);
	}

	public Stream<Document> getAll() {
		return getAll(Set.of());
	}

	public Stream<String> getAllFieldValues(String fieldName) {
		return getAll(Set.of(fieldName)).map(doc -> doc.get(fieldName));
	}

	public Stream<Document> getAll(Set<String> fieldNames) {
		// liveDocs can be null if the reader has no deletions
		Bits liveDocs = MultiBits.getLiveDocs(indexReader);
		return storedFields()
				.map(storedFields -> doGetAll(liveDocs, storedFields, fieldNames))
				.orElseGet(Stream::of);
	}

	public Stream<ScoreAndDocument> search(Query query) throws IOException {
		TopDocsStoredFields topDocsStoredFields = topDocsStoredFields(query);
		return Stream.of(topDocsStoredFields.topDocs().scoreDocs)
				.map(scoreDoc -> safelyGetScoreAndDocument(topDocsStoredFields, scoreDoc))
				.flatMap(Optional::stream);
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
	public void close() {
		IOUtils.closeQuietly(indexReader);
		IOUtils.closeQuietly(directory);
	}

	private record TopDocsStoredFields(TopDocs topDocs, StoredFields storedFields) {
	}
}
