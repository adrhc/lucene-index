package ro.go.adrhc.persistence.lucene.typedindex;

import ro.go.adrhc.persistence.lucene.index.search.SearchResultFilter;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.QuerySearchResult;

public interface QuerySearchResultFilter<F>
		extends SearchResultFilter<QuerySearchResult<F>> {
}
