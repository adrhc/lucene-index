package ro.go.adrhc.persistence.lucene;

import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.operations.ReadDocIndexOperations;
import ro.go.adrhc.persistence.lucene.operations.WriteTypedIndexOperations;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServicesParamsFactory;

import java.io.Closeable;
import java.nio.file.Path;

public interface FileSystemDocTypedIndex<ID, T extends Indexable<ID, T>>
	extends ReadDocIndexOperations<T, ID>, WriteTypedIndexOperations<T, ID>, Closeable {
	IndexServicesParamsFactory<T> getIndexServicesParamsFactory();

	default Path getIndexPath() {
		return getIndexServicesParamsFactory().indexPath();
	}

	default boolean isReadOnly() {
		return getIndexServicesParamsFactory().isReadOnly();
	}
}
