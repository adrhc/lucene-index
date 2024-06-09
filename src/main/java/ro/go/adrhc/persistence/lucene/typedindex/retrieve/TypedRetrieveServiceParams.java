package ro.go.adrhc.persistence.lucene.typedindex.retrieve;

import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.read.OneHitIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParams;

public interface TypedRetrieveServiceParams<T> extends OneHitIndexReaderParams<T> {
	TypedField<T> getIdField();

	TypedIndexReaderParams<T> toAllHitsTypedIndexReaderParams();
}
