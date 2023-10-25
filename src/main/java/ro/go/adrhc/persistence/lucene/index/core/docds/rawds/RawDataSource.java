package ro.go.adrhc.persistence.lucene.index.core.docds.rawds;

import java.io.IOException;
import java.util.Collection;

public interface RawDataSource<ID, T extends Identifiable<ID>> {
	Collection<ID> loadAllIds() throws IOException;

	Collection<T> loadByIds(Collection<ID> ids) throws IOException;

	default Collection<T> loadAll() throws IOException {
		return loadByIds(loadAllIds());
	}
}
