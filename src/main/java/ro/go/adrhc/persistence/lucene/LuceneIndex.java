package ro.go.adrhc.persistence.lucene;

import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.operation.ReadIndexOperations;
import ro.go.adrhc.persistence.lucene.operation.WriteIndexOperations;
import ro.go.adrhc.persistence.lucene.operation.IndexOperationsParams;

import java.io.Closeable;
import java.nio.file.Path;

public interface LuceneIndex<I, T extends Indexable<I, T>>
	extends ReadIndexOperations<T, I>, WriteIndexOperations<T, I>, Closeable {
	IndexOperationsParams<T> getIndexOperationsParams();

	default Path getIndexPath() {
		return getIndexOperationsParams().indexPath();
	}

	default boolean isReadOnly() {
		return getIndexOperationsParams().isReadOnly();
	}
}
