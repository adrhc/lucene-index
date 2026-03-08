package ro.go.adrhc.persistence.lucene.core.typed.write;

import java.io.IOException;
import java.util.stream.Stream;

public interface TypedIndexAdder<T> extends IndexServiceOperations {
	void addOne(T t) throws IOException;

	void addMany(Iterable<T> tIterable) throws IOException;

	void addMany(Stream<T> tStream) throws IOException;
}
