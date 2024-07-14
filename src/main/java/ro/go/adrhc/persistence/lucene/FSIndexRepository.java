package ro.go.adrhc.persistence.lucene;

import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.operations.ReadIndexOperations;
import ro.go.adrhc.persistence.lucene.operations.WriteIndexOperations;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServicesParamsFactory;

import java.io.Closeable;
import java.nio.file.Path;

public interface FSIndexRepository<ID, T extends Indexable<ID, T>>
		extends ReadIndexOperations<T, ID>, WriteIndexOperations<T, ID>, Closeable {
	IndexServicesParamsFactory<T> getIndexServicesParamsFactory();

	default Path getIndexPath() {
		return getIndexServicesParamsFactory().getIndexPath();
	}

	default boolean isReadOnly() {
		return getIndexServicesParamsFactory().isReadOnly();
	}
}
