package ro.go.adrhc.persistence.lucene.typedindex;

import ro.go.adrhc.persistence.lucene.index.search.SearchResultFilter;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.TypedSearchResult;

public interface TypedSearchResultFilter<S, F>
		extends SearchResultFilter<TypedSearchResult<S, F>> {
}
