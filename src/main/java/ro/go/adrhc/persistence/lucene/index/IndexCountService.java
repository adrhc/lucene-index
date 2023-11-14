package ro.go.adrhc.persistence.lucene.index;

import org.apache.lucene.search.Query;

import java.io.IOException;

public interface IndexCountService {
	int count() throws IOException;

	int count(Query query) throws IOException;
}
