package ro.go.adrhc.persistence.lucene.typedindex.search;

import ro.go.adrhc.persistence.lucene.index.search.SearchResultFilter;

public interface TypedSearchResultFilter<S, F>
		extends SearchResultFilter<TypedSearchResult<S, F>> {
}
