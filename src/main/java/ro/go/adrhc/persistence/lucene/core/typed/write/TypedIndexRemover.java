package ro.go.adrhc.persistence.lucene.core.typed.write;

import org.apache.lucene.search.Query;

import java.io.IOException;
import java.util.Collection;

public interface TypedIndexRemover<I> extends IndexServiceOperations {
	void removeOne(I id) throws IOException;

	void removeMany(Collection<? extends I> ids) throws IOException;

	void removeByQuery(Query query) throws IOException;

	void removeAll() throws IOException;
}
