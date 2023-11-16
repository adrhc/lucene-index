package ro.go.adrhc.persistence.lucene.typedindex.reset;

import java.io.IOException;
import java.util.stream.Stream;

import static ro.go.adrhc.util.collection.StreamUtils.stream;

public interface IndexResetService<T> {
	void reset(Stream<T> tStream) throws IOException;

	default void reset(Iterable<T> tIterable) throws IOException {
		reset(stream(tIterable));
	}
}
