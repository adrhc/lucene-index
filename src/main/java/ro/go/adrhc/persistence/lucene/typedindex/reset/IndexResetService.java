package ro.go.adrhc.persistence.lucene.typedindex.reset;

import java.io.IOException;
import java.util.stream.Stream;

import static ro.go.adrhc.util.stream.StreamUtils.stream;

public interface IndexResetService<T> {
	void reset(Stream<T> stateAfterReset) throws IOException;

	default void reset(Iterable<T> stateAfterReset) throws IOException {
		reset(stream(stateAfterReset));
	}
}
