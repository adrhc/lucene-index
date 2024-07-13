package ro.go.adrhc.persistence.lucene.typedindex;

import ro.go.adrhc.persistence.lucene.typedcore.Indexable;
import ro.go.adrhc.persistence.lucene.typedindex.srvparams.IndexServicesParamsFactory;

import java.io.Closeable;
import java.nio.file.Path;

public interface FSIndexRepository<ID, T extends Indexable<ID, T>>
		extends IndexOperations<ID, T>, Closeable {
	IndexServicesParamsFactory<T> getIndexServicesParamsFactory();

	default Path getIndexPath() {
		return getIndexServicesParamsFactory().getIndexPath();
	}

	default boolean isReadOnly() {
		return getIndexServicesParamsFactory().isReadOnly();
	}
}
