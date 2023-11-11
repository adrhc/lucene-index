package ro.go.adrhc.persistence.lucene.typedindex.search.result;

public interface SearchResult<S, F> {
	S getSearched();

	float getScore();

	F getFound();
}
