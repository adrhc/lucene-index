package ro.go.adrhc.persistence.lucene.typedindex.spi;

import java.io.IOException;
import java.util.Collection;

public interface RawDataDatasource<ID, T> {
	Collection<ID> loadAllIds() throws IOException;

	Collection<T> loadByIds(Collection<ID> ids) throws IOException;

	default Collection<T> loadAll() throws IOException {
		return loadByIds(loadAllIds());
	}
}
