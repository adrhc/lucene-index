package ro.go.adrhc.persistence.lucene.core.read;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiBits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Bits;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class DocumentIndexReader implements AutoCloseable {
	private final Directory directory;
	private final IndexReader indexReader;
	private final int maxResultsPerSearchedSong;

	public static DocumentIndexReader of(Path indexPath, int maxResultsPerSearchedSong) throws IOException {
		Directory directory = FSDirectory.open(indexPath);
		IndexReader indexReader = DirectoryReader.open(directory);
		return new DocumentIndexReader(directory, indexReader, maxResultsPerSearchedSong);
	}

	public Stream<String> getAllFieldValues(String fieldName) {
		return getAll(Set.of(fieldName)).map(doc -> doc.get(fieldName));
	}

	public Stream<Document> getAll(Set<String> fieldNames) {
		// liveDocs can be null if the reader has no deletions
		Bits liveDocs = MultiBits.getLiveDocs(indexReader);
		return IntStream.range(0, indexReader.maxDoc())
				.filter(idx -> liveDocs == null || liveDocs.get(idx))
				.mapToObj(idx -> toDocument(fieldNames, idx))
				.flatMap(Optional::stream);
	}

	public Stream<Document> getAll() {
		// liveDocs can be null if the reader has no deletions
		Bits liveDocs = MultiBits.getLiveDocs(indexReader);
		return IntStream.range(0, indexReader.maxDoc())
				.filter(idx -> liveDocs == null || liveDocs.get(idx))
				.mapToObj(this::toDocument)
				.flatMap(Optional::stream);
	}

	public List<ScoreAndDocument> search(Query query) throws IOException {
		IndexSearcher searcher = new IndexSearcher(indexReader);
		TopDocs topDocs = searcher.search(query, maxResultsPerSearchedSong);
		List<ScoreAndDocument> result = new ArrayList<>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			result.add(toScoreAndDocument(searcher, scoreDoc));
		}
		return result;
	}

	@Override
	public void close() {
		IOUtils.closeQuietly(indexReader);
		IOUtils.closeQuietly(directory);
	}

	/**
	 * indexReader.document might fail if the document
	 * is meanwhile purged (not only marked as removed)
	 */
	private Optional<Document> toDocument(Set<String> fieldNames, int docIndex) {
		try {
			return Optional.of(indexReader.document(docIndex, fieldNames));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return Optional.empty();
	}

	private Optional<Document> toDocument(int docIndex) {
		try {
			return Optional.of(indexReader.document(docIndex));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return Optional.empty();
	}

	private ScoreAndDocument toScoreAndDocument(
			IndexSearcher searcher, ScoreDoc scoreDoc) throws IOException {
		return new ScoreAndDocument(scoreDoc.score, searcher.doc(scoreDoc.doc));
	}
}
