package ro.go.adrhc.persistence.lucene.core.typed.field;

import org.apache.lucene.index.IndexableField;
import ro.go.adrhc.persistence.lucene.core.bare.field.FieldType;

import java.util.EnumSet;

public interface LuceneFieldSpec<T> {
	String DEFAULT_ID_FILED_NAME = "id";

	static <E extends Enum<E> & LuceneFieldSpec<?>> E getIdField(Class<E> enumClass) {
		return EnumSet.allOf(enumClass).stream().filter(LuceneFieldSpec::isIdField).findAny()
			.orElseThrow(() -> new IllegalStateException(enumClass + " must have an id field!"));
	}

	String name();

	FieldType fieldType();

	PropertyFieldMapper<T, ?, ?, ?> fieldSerde();

	/**
	 * By default, "id" (case-insensitive) is considered the id field!
	 */
	default boolean isIdField() {
		return DEFAULT_ID_FILED_NAME.equalsIgnoreCase(name());
	}

	/**
	 * Only considered for WORD fields!
	 * <p>
	 * By default, WORD fields are sorted!
	 */
	default boolean isSortable() {
		return false;
	}

	default boolean isPersistent() {
		return isIdField() || fieldType() == FieldType.STORED;
	}

	default <X> X toIndexableValue(T t) {
		Object propValue = fieldSerde().getPropertyValue(t);
		return propToIndexableValue(propValue);
	}

	default <X> X propToIndexableValue(Object propValue) {
		return (X) objectFieldSerde().toIndexableValue(propValue);
	}

	default <P> P indexedValueToPropValue(IndexableField field) {
		Object indexedValue = fieldSerde().getIndexedValue(field);
		return toPropertyValue(indexedValue);
	}

	default <P> P toPropertyValue(Object indexedValue) {
		return (P) objectFieldSerde().toPropertyValue(indexedValue);
	}

	private PropertyFieldMapper<T, Object, Object, Object> objectFieldSerde() {
		return (PropertyFieldMapper<T, Object, Object, Object>) fieldSerde();
	}
}
