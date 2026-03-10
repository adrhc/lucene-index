package ro.go.adrhc.persistence.lucene.core.typed.write;

import java.io.IOException;
import java.util.stream.Stream;

import static ro.go.adrhc.util.stream.StreamUtils.stream;

public interface TypedIndexReset<T> extends BasicIndexOperations {
	void reset(Stream<T> stateAfterReset) throws IOException;

	default void reset(Iterable<T> iterable) throws IOException {
		reset(stream(iterable));
	}
}
