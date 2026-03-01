package ro.go.adrhc.persistence.lucene.operations.search;

import ro.go.adrhc.persistence.lucene.core.bare.read.ScoreDocAndValue;

@FunctionalInterface
public interface SearchResultFilter<T> {
	boolean filter(ScoreDocAndValue<T> found);
}
