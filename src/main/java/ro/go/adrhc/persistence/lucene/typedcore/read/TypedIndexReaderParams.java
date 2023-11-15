package ro.go.adrhc.persistence.lucene.typedcore.read;

import ro.go.adrhc.persistence.lucene.core.read.DocumentsIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;

public interface TypedIndexReaderParams<T> extends DocumentsIndexReaderParams {
	Class<T> getType();

	TypedField<T> getIdField();
}
