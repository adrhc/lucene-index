package ro.go.adrhc.persistence.lucene.typedindex.search;

import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreAndTyped;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParams;

public interface TypedIndexSearchServiceParams<T> extends TypedIndexReaderParams<T> {
	SearchResultFilter<ScoreAndTyped<T>> getSearchResultFilter();
}
