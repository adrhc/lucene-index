package ro.go.adrhc.persistence.lucene.index.count;

import java.io.IOException;

public interface IndexCountService<S> {
	int count() throws IOException;

	int count(S searched) throws IOException;
}
