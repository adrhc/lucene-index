package ro.go.adrhc.persistence.lucene.typedindex.search;

import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreAndValue;

public interface SearchResultFilter<T> {
	boolean filter(ScoreAndValue<T> found);
}
