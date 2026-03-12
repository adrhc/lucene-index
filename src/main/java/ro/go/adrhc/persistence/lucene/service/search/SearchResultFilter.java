package ro.go.adrhc.persistence.lucene.service.search;

import ro.go.adrhc.persistence.lucene.core.typed.read.ScoreAndValue;

@FunctionalInterface
public interface SearchResultFilter<T> {
	boolean filter(ScoreAndValue<T> found);
}
