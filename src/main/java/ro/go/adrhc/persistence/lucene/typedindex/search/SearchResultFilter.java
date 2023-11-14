package ro.go.adrhc.persistence.lucene.typedindex.search;

public interface SearchResultFilter<T> {
	boolean filter(T found);
}
