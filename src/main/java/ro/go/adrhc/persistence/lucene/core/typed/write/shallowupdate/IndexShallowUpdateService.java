package ro.go.adrhc.persistence.lucene.core.typed.write.shallowupdate;

import org.apache.lucene.search.Query;

import java.io.IOException;

/**
 * Change the index to only have the IndexDataSource data; remove (by id) the surplus, add missing.
 */
public interface IndexShallowUpdateService<I, T> {
	/**
	 * add the missing and remove the surplus
	 */
	void shallowUpdate(IndexDataSource<I, T> dataSource) throws IOException;

	/**
	 * add the missing and remove the surplus, all relative to the subset determined by query
	 */
	void shallowUpdateSubset(IndexDataSource<I, T> dataSource, Query query) throws IOException;
}
