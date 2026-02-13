package ro.go.adrhc.persistence.lucene.core.typed.read;

import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;

public interface OneHitIndexReaderParams<T> {
	IndexReaderPool indexReaderPool();

	Class<T> type();
}
