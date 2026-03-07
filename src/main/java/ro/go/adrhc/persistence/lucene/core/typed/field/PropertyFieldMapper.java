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
	Function<IndexableField, ?> indexedValueAccessor,
	/*
	 * Converts the value obtained from the index to the property value type (P of T).
	 */
	Function<?, P> indexedValueToPropertyValueConverter) {

	public P getPropertyValue(T t) {
		return propertyAccessor.apply(t);
	}

	public <X> X toIndexableValue(Object propertyValue) {
		return (X) propertyToIndexableValueConverter.apply(propertyValue);
	}

	public <X> X getIndexedValue(IndexableField indexableField) {
		return (X) indexedValueAccessor.apply(indexableField);
	}

	public P toPropertyValue(Object indexedValue) {
		return ((Function<Object, P>) indexedValueToPropertyValueConverter).apply(indexedValue);
	}
}
