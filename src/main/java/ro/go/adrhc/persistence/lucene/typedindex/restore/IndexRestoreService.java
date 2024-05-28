package ro.go.adrhc.persistence.lucene.typedindex.restore;

import org.apache.lucene.search.Query;

import java.io.IOException;

/**
 * Change the index to only have the IndexDataSource data; remove (by id) the surplus, add missing.
 */
public interface IndexRestoreService<ID, T> {
	void restore(IndexDataSource<ID, T> dataSource) throws IOException;

	void restoreSubset(IndexDataSource<ID, T> dataSource, Query query) throws IOException;
}
