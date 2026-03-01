package ro.go.adrhc.persistence.lucene.operations.count;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;

import java.io.IOException;

public interface IndexCountService {
	boolean isEmpty() throws IOException;

	int count() throws IOException;

	boolean hasAfter(ScoreDoc scoreDoc, Sort sort) throws IOException;

	int count(Query query) throws IOException;
}
