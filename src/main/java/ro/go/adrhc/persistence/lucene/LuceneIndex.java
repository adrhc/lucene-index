package ro.go.adrhc.persistence.lucene;

import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.operations.ReadIndexOperations;
import ro.go.adrhc.persistence.lucene.operations.WriteIndexOperations;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServiceParamsFactory;

import java.io.Closeable;
import java.nio.file.Path;

public interface LuceneIndex<I, T extends Indexable<I, T>>
	extends ReadIndexOperations<T, I>, WriteIndexOperations<T, I>, Closeable {
	IndexServiceParamsFactory<T> getIndexServiceParamsFactory();

	default Path getIndexPath() {
		return getIndexServiceParamsFactory().indexPath();
	}

	default boolean isReadOnly() {
		return getIndexServiceParamsFactory().isReadOnly();
	}
}
