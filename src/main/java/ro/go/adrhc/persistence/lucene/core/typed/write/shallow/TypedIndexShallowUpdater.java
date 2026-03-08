package ro.go.adrhc.persistence.lucene.core.typed.write.shallow;

import org.apache.lucene.search.Query;

import java.io.IOException;

/**
 * Change the index to only have the TypedIndexDataSource data; remove (by id) the surplus, add missing.
 */
public interface TypedIndexShallowUpdater<I, T> {
	/**
	 * add the missing and remove the surplus
	 */
	void shallowUpdate(TypedIndexDataSource<I, T> dataSource) throws IOException;

	/**
	 * add the missing and remove the surplus, all relative to the subset determined by query
	 */
	void shallowUpdateSubset(TypedIndexDataSource<I, T> dataSource, Query query) throws IOException;
}
