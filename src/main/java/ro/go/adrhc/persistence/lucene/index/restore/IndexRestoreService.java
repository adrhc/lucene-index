package ro.go.adrhc.persistence.lucene.index.restore;

import ro.go.adrhc.persistence.lucene.index.core.docds.datasource.IndexDataSource;

import java.io.IOException;

public interface IndexRestoreService<ID, T> {
	void restore(IndexDataSource<ID, T> dataSource) throws IOException;
}
