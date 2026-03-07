package ro.go.adrhc.persistence.lucene.core.typed.field;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexableField;

import java.util.function.Function;

/**
 * @param <T> is the type of the object containing the property
 * @param <P> is the type of the property value
 * @param <X> is the indexable value, e.g., String, Set<String>, Integer, Long, etc.
 * @param <F> is the indexed value, present in a IndexableField, e.g., String, Integer, Long, etc.
 */
@Slf4j
public record PropertyFieldMapper<T, P, X, F>(
	/*
	 * Extracts the property value (P) from the object (T).
	 */
	Function<T, P> propertyAccessor,
	Function<P, X> propertyToIndexableValueConverter,
	Function<IndexableField, F> indexedValueAccessor,
	/*
	 * Converts the value obtained from the index to the property value type (P of T).
	 */
	Function<F, P> indexedValueToPropertyValueConverter) {

	public P getPropertyValue(T t) {
		return propertyAccessor.apply(t);
	}

	public X toIndexableValue(P propertyValue) {
		return propertyToIndexableValueConverter.apply(propertyValue);
	}

	public F getIndexedValue(IndexableField indexableField) {
		return indexedValueAccessor.apply(indexableField);
	}

	public P toPropertyValue(F indexedValue) {
		return indexedValueToPropertyValueConverter.apply(indexedValue);
	}
}
