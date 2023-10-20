package ro.go.adrhc.persistence.lucene.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import ro.go.adrhc.util.Assert;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public record DocumentIndexReaderTemplate(int maxResultsPerSearchedSong, Path indexPath) {
	public <R, E extends Exception> R transformFieldStream(String fieldName,
			SneakyFunction<Stream<String>, R, E> fieldStreamTransformer) throws IOException, E {
		return useReader(indexReader -> fieldStreamTransformer.apply(indexReader.fieldStream(fieldName)));
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
