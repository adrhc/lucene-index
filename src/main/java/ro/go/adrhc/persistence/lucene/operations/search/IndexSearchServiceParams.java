package ro.go.adrhc.persistence.lucene.operations.search;

import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.read.OneHitIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.read.OneHitIndexReaderParamsImpl;

import static ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParamsImpl.allHits;

public interface IndexSearchServiceParams<T> extends HitsLimitedIndexReaderParams<T> {
	SearchResultFilter<T> getSearchResultFilter();

	default OneHitIndexReaderParams<T> oneHitIndexReaderParams() {
		return new OneHitIndexReaderParamsImpl<>(getIndexReaderPool(), getType());
	}

	default HitsLimitedIndexReaderParams<T> allHitsTypedIndexReaderParams() {
		return allHits(getType(), getIdField(), getIndexReaderPool());
	}
}
