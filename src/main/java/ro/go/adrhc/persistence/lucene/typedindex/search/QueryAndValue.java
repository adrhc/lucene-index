package ro.go.adrhc.persistence.lucene.typedindex.search;

import org.apache.lucene.search.Query;

public record QueryAndValue<T>(Query query, T value) {
	public static <T> QueryAndValue<T> of(QueryAndScoreAndValue<T> typedSearchResult) {
		return new QueryAndValue<>(typedSearchResult.query(), typedSearchResult.value());
	}
}
