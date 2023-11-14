package ro.go.adrhc.persistence.lucene.typedcore.read;

import ro.go.adrhc.persistence.lucene.core.read.DocumentsIndexReaderParams;

public interface TypedIndexReaderParams<T> extends DocumentsIndexReaderParams {
	Class<T> getType();
}
