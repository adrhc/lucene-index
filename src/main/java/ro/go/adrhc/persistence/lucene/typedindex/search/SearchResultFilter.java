package ro.go.adrhc.persistence.lucene.typedindex.search;

import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreAndTyped;

public interface SearchResultFilter<T> {
	boolean filter(ScoreAndTyped<T> found);
}
