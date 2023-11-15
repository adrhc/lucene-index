package ro.go.adrhc.persistence.lucene.typedcore.read;

import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;

public interface OneHitIndexReaderParams<T> {
	IndexReaderPool getIndexReaderPool();

	Class<T> getType();
}
