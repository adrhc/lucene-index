package ro.go.adrhc.persistence.lucene.typedindex.update;

import java.io.IOException;

public interface IndexUpsertService<T> {
	void upsert(T t) throws IOException;

	void upsertMany(Iterable<T> iterable) throws IOException;
}
