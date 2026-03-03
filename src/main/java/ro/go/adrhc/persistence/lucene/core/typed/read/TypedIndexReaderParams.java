package ro.go.adrhc.persistence.lucene.core.typed.read;

import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexReaderParams;

public interface TypedIndexReaderParams<T> extends DocIndexReaderParams {
	Class<T> type();
}
