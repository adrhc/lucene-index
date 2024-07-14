package ro.go.adrhc.persistence.lucene.operations.search;

import ro.go.adrhc.persistence.lucene.core.typed.read.ScoreDocAndValue;

public interface SearchResultFilter<T> {
	boolean filter(ScoreDocAndValue<T> found);
}
