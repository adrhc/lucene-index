package ro.go.adrhc.persistence.lucene.typedindex.update;

import java.io.IOException;

public interface IndexUpdateService<T> {
	void update(T t) throws IOException;
}
