package ro.go.adrhc.persistence.lucene.typedcore.write;

import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;

public interface TypedIndexUpdaterParams<T> extends AbstractTypedIndexParams<T> {
    TypedField<T> getIdField();
}
