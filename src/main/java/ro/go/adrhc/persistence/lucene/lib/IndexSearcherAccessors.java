package ro.go.adrhc.persistence.lucene.lib;

import lombok.experimental.UtilityClass;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;

@UtilityClass
public class IndexSearcherAccessors {
	public static TopDocs search(IndexSearcher searcher,
		Query query, int numHits, Sort sort) throws IOException {
		if (sort == null) {
			return searcher.search(query, numHits);
		} else {
			return searcher.search(query, numHits, sort);
		}
	}
}
