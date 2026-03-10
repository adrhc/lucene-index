package ro.go.adrhc.persistence.lucene.core.typed.write;

import ro.go.adrhc.persistence.lucene.core.typed.Identifiable;

import java.io.IOException;
import java.util.Collection;

public interface TypedIndexUpsert<T extends Identifiable<?>> extends BasicIndexOperations {
	void upsert(T t) throws IOException;

	void upsertMany(Collection<T> tCollection) throws IOException;
}
