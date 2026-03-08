package ro.go.adrhc.persistence.lucene.core.bare.read;

import org.apache.lucene.search.Query;

import java.io.IOException;

public interface DocIndexCounter {
	boolean isEmpty() throws IOException;

	int count() throws IOException;

	int count(Query query) throws IOException;
}
