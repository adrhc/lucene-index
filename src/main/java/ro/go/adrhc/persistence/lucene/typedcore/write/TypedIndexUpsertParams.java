package ro.go.adrhc.persistence.lucene.typedcore.write;

import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;

public interface TypedIndexUpsertParams<T> extends TypedIndexWriterParams<T> {
	TypedField<T> getIdField();
}
