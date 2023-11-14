package ro.go.adrhc.persistence.lucene.typedindex.search;

import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParams;

public interface TypedSearchByIdServiceParams<T> extends TypedIndexReaderParams<T> {
	TypedField<?> getIdField();
}
