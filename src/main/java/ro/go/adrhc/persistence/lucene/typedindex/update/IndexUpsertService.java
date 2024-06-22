package ro.go.adrhc.persistence.lucene.typedindex.update;

import java.io.IOException;
import java.util.Collection;

public interface IndexUpsertService<T> {
	void upsert(T t) throws IOException;

	void upsertMany(Collection<T> tCollection) throws IOException;
}
