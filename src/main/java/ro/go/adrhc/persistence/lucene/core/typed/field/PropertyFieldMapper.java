package ro.go.adrhc.persistence.lucene.core.typed.field;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexableField;

import java.util.function.Function;

@Slf4j
public record PropertyFieldMapper<T, P>(
	/*
	 * Extracts the property value (P) from the object (T).
	 */
	Function<T, P> propertyAccessor,
	Function<Object, ?> propertyToIndexableValueConverter,
	Function<IndexableField, Object> indexedValueAccessor,
	/*
	 * Converts the value obtained from the index to the property value type (P of T).
	 */
	Function<Object, P> indexedValueToPropertyValue) {

	public Object getIndexedValue(IndexableField indexableField) {
		return indexedValueAccessor.apply(indexableField);
	}

	public P getPropertyValue(T t) {
		return propertyAccessor.apply(t);
	}

	public Object toIndexableValue(Object propertyValue) {
		return propertyToIndexableValueConverter.apply(propertyValue);
	}

	public P toPropertyValue(Object indexedValue) {
		return indexedValueToPropertyValue.apply(indexedValue);
	}
}
