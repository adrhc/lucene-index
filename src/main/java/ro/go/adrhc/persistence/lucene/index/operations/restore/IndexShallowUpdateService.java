package ro.go.adrhc.persistence.lucene.index.operations.restore;

import org.apache.lucene.search.Query;

import java.io.IOException;

/**
 * Change the index to only have the IndexDataSource data; remove (by id) the surplus, add missing.
 */
public interface IndexShallowUpdateService<ID, T> {
	/**
	 * add the missing and remove the surplus
	 */
	void shallowUpdate(IndexDataSource<ID, T> dataSource) throws IOException;

	/**
	 * add the missing and remove the surplus, all relative to the subset determined by query
	 */
	void shallowUpdateSubset(IndexDataSource<ID, T> dataSource, Query query) throws IOException;
}
