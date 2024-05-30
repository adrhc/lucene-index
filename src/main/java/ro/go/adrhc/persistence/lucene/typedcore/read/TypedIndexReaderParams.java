package ro.go.adrhc.persistence.lucene.typedcore.read;

import ro.go.adrhc.persistence.lucene.core.read.HitsLimitedDocsIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;

public interface TypedIndexReaderParams<T> extends HitsLimitedDocsIndexReaderParams {
	Class<T> getType();

	TypedField<T> getIdField();
}
