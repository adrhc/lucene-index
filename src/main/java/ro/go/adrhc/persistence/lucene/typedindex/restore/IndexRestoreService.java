package ro.go.adrhc.persistence.lucene.typedindex.restore;

import ro.go.adrhc.persistence.lucene.typedindex.core.indexds.IndexDataSource;

import java.io.IOException;

public interface IndexRestoreService<ID, T> {
	void restore(IndexDataSource<ID, T> dataSource) throws IOException;
}
