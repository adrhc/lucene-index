package ro.go.adrhc.persistence.lucene.core.typed.write.shallow;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

public interface TypedIndexDataSource<I, T> {
	Stream<I> loadAllIds() throws IOException;

	Stream<T> loadByIds(Stream<I> ids) throws IOException;

	default Stream<T> loadAll() throws IOException {
		return loadByIds(loadAllIds());
	}

	default Stream<T> loadByIds(Collection<I> ids) throws IOException {
		return loadByIds(ids.stream());
	}
}
