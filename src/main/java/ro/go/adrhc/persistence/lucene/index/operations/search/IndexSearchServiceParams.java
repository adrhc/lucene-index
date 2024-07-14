package ro.go.adrhc.persistence.lucene.index.operations.search;

import ro.go.adrhc.persistence.lucene.typedcore.read.OneHitIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.read.OneHitIndexReaderParamsImpl;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParams;

import static ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParamsImpl.allHits;

public interface IndexSearchServiceParams<T> extends TypedIndexReaderParams<T> {
	SearchResultFilter<T> getSearchResultFilter();

	default OneHitIndexReaderParams<T> oneHitIndexReaderParams() {
		return new OneHitIndexReaderParamsImpl<>(getIndexReaderPool(), getType());
	}

	default TypedIndexReaderParams<T> allHitsTypedIndexReaderParams() {
		return allHits(getType(), getIdField(), getIndexReaderPool());
	}
}
