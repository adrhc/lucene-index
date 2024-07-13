package ro.go.adrhc.persistence.lucene.typedindex;

import ro.go.adrhc.persistence.lucene.typedcore.Indexable;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexServicesParamsFactory;

import java.io.Closeable;
import java.nio.file.Path;

public interface IndexRepository<ID, T extends Indexable<ID, T>>
		extends IndexOperations<ID, T>, Closeable {
	TypedIndexServicesParamsFactory<T> getTypedIndexParams();

	default Path getIndexPath() {
		return getTypedIndexParams().getIndexPath();
	}

	default boolean isReadOnly() {
		return getTypedIndexParams().isReadOnly();
	}
}
