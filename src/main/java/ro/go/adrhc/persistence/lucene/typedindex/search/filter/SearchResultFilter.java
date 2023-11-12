package ro.go.adrhc.persistence.lucene.typedindex.search.filter;

public interface SearchResultFilter<F> {
	boolean filter(F found);
}
