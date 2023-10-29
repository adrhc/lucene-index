package ro.go.adrhc.persistence.lucene.index;

import java.io.IOException;
import java.util.stream.Stream;

import static ro.go.adrhc.util.collection.StreamUtils.stream;

public interface IndexCreateService<T> {
	void createOrReplace(Stream<T> tStream) throws IOException;

	default void createOrReplace(Iterable<T> tIterable) throws IOException {
		createOrReplace(stream(tIterable));
	}
}
