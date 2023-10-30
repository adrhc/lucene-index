package ro.go.adrhc.persistence.lucene.index.restore;

import java.io.IOException;

public interface IndexRestoreService<ID, T> {
	void restore(IndexDataSource<ID, T> dataSource) throws IOException;
}
