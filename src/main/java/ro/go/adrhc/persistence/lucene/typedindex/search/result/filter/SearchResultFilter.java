package ro.go.adrhc.persistence.lucene.typedindex.search.result.filter;

public interface SearchResultFilter<F> {
	boolean filter(F found);
}
