package ro.go.adrhc.persistence.lucene.typedcore.field;

import org.apache.lucene.index.IndexableField;
import ro.go.adrhc.persistence.lucene.core.field.FieldType;

import java.util.EnumSet;

public interface TypedField<T> {
	static <E extends Enum<E> & TypedField<?>> E getIdField(Class<E> enumClass) {
		return EnumSet.allOf(enumClass).stream().filter(TypedField::isIdField).findAny()
				.orElseThrow(() -> new NullPointerException(enumClass + " must have an id field!"));
	}

	TypedFieldSerde<T, ?> fieldSerde();

	String name();

	boolean isIdField();

	FieldType fieldType();

	default boolean mustStore() {
		return isIdField() || fieldType() == FieldType.STORED;
	}

	default Object propToIndexableValue(Object propValue) {
		return fieldSerde().toFieldValue().apply(propValue);
	}

	default Object typedToIndexableValue(T t) {
		Object propValue = fieldSerde().propertyAccessor().apply(t);
		return propToIndexableValue(propValue);
	}

	default Object toPropValue(Object indexableValue) {
		return fieldSerde().toPropertyValue().apply(indexableValue);
	}

	default Object indexableFieldToPropValue(IndexableField field) {
		Object indexedValue = fieldSerde().fieldValueAccessor().apply(field);
		return toPropValue(indexedValue);
	}
}
