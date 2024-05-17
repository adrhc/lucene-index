package ro.go.adrhc.persistence.lucene.typedcore.read;

import ro.go.adrhc.persistence.lucene.core.read.DocsIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;

public interface TypedIndexReaderParams<T> extends DocsIndexReaderParams {
	Class<T> getType();

	TypedField<T> getIdField();
}
