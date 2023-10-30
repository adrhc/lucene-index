package ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

public interface RawDataSource<ID, T extends Identifiable<ID>> {
	Stream<ID> loadAllIds() throws IOException;

	Stream<T> loadByIds(Stream<ID> ids) throws IOException;

	default Stream<T> loadAll() throws IOException {
		return loadByIds(loadAllIds());
	}

	default Stream<T> loadByIds(Collection<ID> ids) throws IOException {
		return loadByIds(ids.stream());
	}
}
