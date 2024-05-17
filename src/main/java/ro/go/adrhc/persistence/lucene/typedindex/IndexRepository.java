package ro.go.adrhc.persistence.lucene.typedindex;

import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;

import java.io.Closeable;

public interface IndexRepository<ID, T extends Identifiable<ID>>
		extends IndexOperations<ID, T>, Closeable {
}
