package ro.go.adrhc.persistence.lucene.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import ro.go.adrhc.util.Assert;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public record DocumentIndexReaderTemplate(int maxResultsPerSearchedSong, Path indexPath) {
	/**
	 * Make sure that songsIndexReaderFn does not return a Stream! at the moment
	 * the Stream will actually run the DocumentIndexReader shall already be closed.
	 */
	public <U, E extends Exception> U useReader(
			SneakyFunction<? super DocumentIndexReader, ? extends U, E> indexReaderFn)
			throws E, IOException {
		try (DocumentIndexReader indexReader = DocumentIndexReader.of(indexPath, maxResultsPerSearchedSong)) {
			U u = indexReaderFn.apply(indexReader);
			Assert.isTrue(!(u instanceof Stream<?>), "Result must not be a stream!");
			return u;
		}
	}
}
