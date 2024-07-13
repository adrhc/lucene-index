package ro.go.adrhc.persistence.lucene.typedcore.field;

import org.apache.lucene.index.IndexableField;
import ro.go.adrhc.persistence.lucene.core.field.FieldType;

import java.util.EnumSet;

public interface TypedField<T> {
	static <E extends Enum<E> & TypedField<?>> E getIdField(Class<E> enumClass) {
		return EnumSet.allOf(enumClass).stream().filter(TypedField::isIdField).findAny()
				.orElseThrow(() -> new NullPointerException(enumClass + " must have an id field!"));
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
		Object typedValue = fieldSerde().propertyAccessor().apply(t);
		return fieldSerde().toFieldValue().apply(typedValue);
	}

	default Object indexableFieldToTypedValue(IndexableField field) {
		Object indexedValue = fieldSerde().fieldAccessor().apply(field);
		return fieldSerde().toPropertyValue().apply(indexedValue);
	}

	default Object toTypedValue(Object indexedValue) {
		return fieldSerde().toPropertyValue().apply(indexedValue);
	}
}
