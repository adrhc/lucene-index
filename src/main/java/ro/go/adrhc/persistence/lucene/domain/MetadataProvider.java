package ro.go.adrhc.persistence.lucene.domain;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface MetadataProvider<MID, M> {
	List<MID> loadAllIds() throws IOException;

	List<M> loadByIds(Collection<MID> paths) throws IOException;

	default List<M> loadAll() throws IOException {
		return loadByIds(loadAllIds());
	}
}
