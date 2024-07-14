package ro.go.adrhc.persistence.lucene.index;

import ro.go.adrhc.persistence.lucene.index.operations.ReadIndexOperations;
import ro.go.adrhc.persistence.lucene.index.operations.WriteIndexOperations;
import ro.go.adrhc.persistence.lucene.index.operations.params.IndexServicesParamsFactory;
import ro.go.adrhc.persistence.lucene.typedcore.Indexable;

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
