package ro.go.adrhc.persistence.lucene.core.typed.field;

import org.apache.lucene.index.IndexableField;
import ro.go.adrhc.persistence.lucene.core.bare.field.FieldType;

import java.util.EnumSet;

public interface LuceneFieldSpec<T> {
	static <E extends Enum<E> & LuceneFieldSpec<?>> E getIdField(Class<E> enumClass) {
		return EnumSet.allOf(enumClass).stream().filter(LuceneFieldSpec::isIdField).findAny()
			.orElseThrow(() -> new NullPointerException(enumClass + " must have an id field!"));
	}

	ObjectLuceneFieldMapper<T, ?> fieldSerde();

	String name();

	boolean isIdField();

	FieldType fieldType();

	default boolean mustStore() {
		return isIdField() || fieldType() == FieldType.STORED;
	}

	default Object typedToIndexableValue(T t) {
		Object propValue = fieldSerde().propertyAccessor().apply(t);
		return propToIndexableValue(propValue);
	}

	default Object propToIndexableValue(Object propValue) {
		return fieldSerde().toIndexableValue().apply(propValue);
	}

	default Object indexableValueToPropValue(IndexableField field) {
		Object indexedValue = fieldSerde().indexedValueAccessor().apply(field);
		return toPropValue(indexedValue);
	}

	default Object toPropValue(Object indexableValue) {
		return fieldSerde().toPropertyValue().apply(indexableValue);
	}
}
