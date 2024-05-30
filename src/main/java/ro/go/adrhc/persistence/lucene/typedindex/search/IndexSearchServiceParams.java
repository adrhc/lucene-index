package ro.go.adrhc.persistence.lucene.typedindex.search;

import ro.go.adrhc.persistence.lucene.typedcore.read.DefaultOneHitIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.read.OneHitIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParams;

import static ro.go.adrhc.persistence.lucene.typedcore.read.DefaultTypedIndexReaderParams.allHits;

public interface IndexSearchServiceParams<T> extends TypedIndexReaderParams<T> {
	SearchResultFilter<T> getSearchResultFilter();

	default OneHitIndexReaderParams<T> toOneHitIndexReaderParams() {
		return new DefaultOneHitIndexReaderParams<>(getIndexReaderPool(), getType());
	}

	default TypedIndexReaderParams<T> toAllHitsTypedIndexReaderParams() {
		return allHits(getType(), getIdField(), getIndexReaderPool());
	}
}
