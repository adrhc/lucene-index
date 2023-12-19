package ro.go.adrhc.persistence.lucene.typedcore.field;

import org.apache.lucene.index.IndexableField;
import ro.go.adrhc.persistence.lucene.core.field.FieldType;
import ro.go.adrhc.util.Assert;

import java.util.EnumSet;
import java.util.Optional;

public interface TypedField<T> {
    static <E extends Enum<E> & TypedField<?>> E getIdField(Class<E> enumClass) {
        Optional<E> id = EnumSet.allOf(enumClass).stream().filter(TypedField::isIdField).findAny();
        Assert.isTrue(id.isPresent(), enumClass + " must have an id field!");
        return id.get();
    }

    TypedFieldSerde<T> fieldSerde();

    String name();

    boolean isIdField();

    FieldType fieldType();

    default boolean mustStore() {
        return isIdField() || fieldType() == FieldType.STORED;
    }

    default Object toIndexableFieldValue(Object typedValue) {
        return fieldSerde().toFieldValue().apply(typedValue);
    }

    default Object typedToIndexableFieldValue(T t) {
        Object typedValue = fieldSerde().typedAccessor().apply(t);
        return fieldSerde().toFieldValue().apply(typedValue);
    }

    default Object indexableFieldToTypedValue(IndexableField field) {
        Object indexableFieldValue = fieldSerde().indexableFieldAccessor().apply(field);
        return fieldSerde().toTypedValue().apply(indexableFieldValue);
    }
}
