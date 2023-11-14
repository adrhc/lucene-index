package ro.go.adrhc.persistence.lucene.typedindex.create;

import java.io.IOException;
import java.util.stream.Stream;

import static ro.go.adrhc.util.collection.StreamUtils.stream;

public interface IndexInitService<T> {
	void initialize(Stream<T> tStream) throws IOException;

	default void initialize(Iterable<T> tIterable) throws IOException {
		initialize(stream(tIterable));
	}
}
