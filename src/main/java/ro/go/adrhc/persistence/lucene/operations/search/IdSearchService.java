package ro.go.adrhc.persistence.lucene.operations.search;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;

import java.io.IOException;

public interface IdSearchService<ID> {
	ScoreDocAndValues<ID> findManyIds(Query query, Sort sort) throws IOException;
}
