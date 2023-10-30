package ro.go.adrhc.persistence.lucene.typedindex.domain.seach;

import org.apache.lucene.search.Query;

public class QuerySearchResult<F> extends TypedSearchResult<Query, F> {
	public QuerySearchResult(Query searched, float score, F found) {
		super(searched, score, found);
	}
}
