package ro.go.adrhc.persistence.lucene.index.operations.search;

import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreDocAndValue;

public interface SearchResultFilter<T> {
	boolean filter(ScoreDocAndValue<T> found);
}
