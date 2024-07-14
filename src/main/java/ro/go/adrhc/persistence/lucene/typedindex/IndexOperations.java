package ro.go.adrhc.persistence.lucene.typedindex;

import ro.go.adrhc.persistence.lucene.typedcore.Indexable;

/**
 * This is a facade for all index services.
 */
public interface IndexOperations<ID, T extends Indexable<ID, T>>
		extends ReadIndexOperations<T, ID>, WriteIndexOperations<T, ID> {
}
