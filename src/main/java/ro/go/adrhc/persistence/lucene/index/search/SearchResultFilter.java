package ro.go.adrhc.persistence.lucene.index.search;

public interface SearchResultFilter<F> {
	boolean filter(F found);
}
