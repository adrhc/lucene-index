package ro.go.adrhc.persistence.lucene.typedindex.search;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;

import java.io.IOException;
import java.util.List;

public interface SearchManyService<T> {
	ScoreDocAndValues<T> findMany(Query query, int hitsCount, Sort sort) throws IOException;

	List<T> findMany(Query query) throws IOException;

	ScoreDocAndValues<T> findManyAfter(ScoreDoc after,
			Query query, int hitsCount, Sort sort) throws IOException;
}
