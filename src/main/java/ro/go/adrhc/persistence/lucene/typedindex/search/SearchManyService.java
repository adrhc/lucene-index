package ro.go.adrhc.persistence.lucene.typedindex.search;

import org.apache.lucene.search.Query;

import java.io.IOException;
import java.util.List;

public interface SearchManyService<T> {
	List<T> findAllMatches(Query query) throws IOException;
}
