package ro.go.adrhc.persistence.lucene.typedindex.domain.seach;

public interface SearchResult<S, F> {
	S getSearched();

	float getScore();

	F getFound();
}
