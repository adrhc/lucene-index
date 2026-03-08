package ro.go.adrhc.persistence.lucene.operations.search;

import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderParams;

public interface IndexSearchServiceParams<T> extends HitsLimitedIndexReaderParams<T> {
	SearchResultFilter<T> searchResultFilter();

	TypedIndexReaderParams<T> typedIndexReaderParams();

	HitsLimitedIndexReaderParams<T> allHitsIndexReaderParams();
}
