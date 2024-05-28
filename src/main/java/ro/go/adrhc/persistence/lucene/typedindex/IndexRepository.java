package ro.go.adrhc.persistence.lucene.typedindex;

import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexParams;

import java.io.Closeable;
import java.nio.file.Path;

public interface IndexRepository<ID, T extends Identifiable<ID>>
		extends IndexOperations<ID, T>, Closeable {
	TypedIndexParams<T> getTypedIndexParams();

	Path getIndexPath();
}
