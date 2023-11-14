package ro.go.adrhc.persistence.lucene.typedindex.remove;

import java.io.IOException;
import java.util.Collection;

public interface IndexRemoveService<ID> {
	void removeByIds(Collection<ID> ids) throws IOException;

	void removeById(ID id) throws IOException;
}
