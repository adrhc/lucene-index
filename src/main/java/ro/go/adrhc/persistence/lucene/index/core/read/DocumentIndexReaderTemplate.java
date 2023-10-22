package ro.go.adrhc.persistence.lucene.index.core.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import org.apache.lucene.document.Document;
import ro.go.adrhc.util.Assert;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public record DocumentIndexReaderTemplate(int maxResultsPerSearchedSong, Path indexPath) {
	public <R, E extends Exception> R transformFieldValues(String fieldName,
			SneakyFunction<Stream<String>, R, E> fieldValuesTransformer) throws IOException, E {
		return useReader(indexReader -> fieldValuesTransformer.apply(indexReader.getAllFieldValues(fieldName)));
	}

	public <R, E extends Exception> R transformDocuments(
			SneakyFunction<Stream<Document>, R, E> documentsTransformer) throws IOException, E {
		return useReader(indexReader -> documentsTransformer.apply(indexReader.getAll()));
	}

	/**
	 * Make sure that songsIndexReaderFn does not return a Stream! at the moment
	 * the Stream will actually run the DocumentIndexReader shall already be closed.
	 */
	public <R, E extends Exception> R useReader(
			SneakyFunction<DocumentIndexReader, R, E> indexReaderFn)
			throws IOException, E {
		try (DocumentIndexReader indexReader = DocumentIndexReader.of(indexPath, maxResultsPerSearchedSong)) {
			R result = indexReaderFn.apply(indexReader);
			Assert.isTrue(!(result instanceof Stream<?>), "Result must not be a stream!");
			return result;
		}
	}
}
