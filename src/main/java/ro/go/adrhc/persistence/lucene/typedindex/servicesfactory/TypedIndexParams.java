package ro.go.adrhc.persistence.lucene.typedindex.servicesfactory;

import ro.go.adrhc.persistence.lucene.typedcore.write.AbstractTypedIndexParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexUpdaterParams;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedIndexRetrieveServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchServiceParams;

import java.io.Closeable;
import java.nio.file.Path;

public interface TypedIndexParams<ID, T> extends TypedIndexSearchServiceParams<T>,
		TypedIndexRestoreServiceParams<ID, T>, TypedIndexRetrieveServiceParams<T>,
		TypedIndexUpdaterParams<T>, AbstractTypedIndexParams<T>,
		TypedIndexRemoverParams, Closeable {
	Path getIndexPath();

	boolean isReadOnly();
}
