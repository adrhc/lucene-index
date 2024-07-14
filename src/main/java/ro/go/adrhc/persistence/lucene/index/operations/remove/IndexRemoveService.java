package ro.go.adrhc.persistence.lucene.index.operations.remove;

import org.apache.lucene.search.Query;

import java.io.IOException;
import java.util.Collection;

public interface IndexRemoveService<ID> {
	void removeByIds(Collection<ID> ids) throws IOException;

	void removeById(ID id) throws IOException;

	void removeByQuery(Query query) throws IOException;
}
