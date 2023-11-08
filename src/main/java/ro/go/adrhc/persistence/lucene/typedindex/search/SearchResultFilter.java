package ro.go.adrhc.persistence.lucene.typedindex.search;

public interface SearchResultFilter<F> {
	boolean filter(F found);
}
