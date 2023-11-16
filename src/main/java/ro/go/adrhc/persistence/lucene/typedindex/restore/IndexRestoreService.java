package ro.go.adrhc.persistence.lucene.typedindex.restore;

import java.io.IOException;

/**
 * Change the index to only have the IndexDataSource data; remove (by id) the surplus, add missing.
 */
public interface IndexRestoreService<ID, T> {
	void restore(IndexDataSource<ID, T> dataSource) throws IOException;
}
