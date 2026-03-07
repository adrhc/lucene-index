package ro.go.adrhc.persistence.lucene.core.typed.field;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexableField;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Set;
import java.util.function.Function;

@Slf4j
public record PropertyFieldMapper<T, P>(
	/*
	 * Extracts the property value (P) from the object (T).
	 */
	Function<T, P> propertyAccessor,
	Function<Object, ?> toIndexableValue,
	Function<IndexableField, Object> indexedValueAccessor,
	/*
	 * Converts the value obtained from the index to the property value type (P of T).
	 */
	Function<Object, P> toPropertyValue) {

	public static <T, P> PropertyFieldMapper<T, P> stringField(
		Function<T, P> propertyAccessor,
		Function<Object, P> indexedValueToPropValue) {
		return new PropertyFieldMapper<>(propertyAccessor, PropertyFieldMapper::toString,
			IndexableField::stringValue, indexedValueToPropValue);
	}

	public static <T> PropertyFieldMapper<T, String>
	stringField(Function<T, String> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor,
			it -> it, IndexableField::stringValue, it -> (String) it);
	}

	public static <T> PropertyFieldMapper<T, URI> uriField(Function<T, URI> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor, PropertyFieldMapper::toString,
			IndexableField::stringValue, PropertyFieldMapper::toURI);
	}

	public static <T> PropertyFieldMapper<T, Boolean> booleanField(
		Function<T, Boolean> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor,
			it -> it != null && ((Boolean) it) ? 1 : 0,
			PropertyFieldMapper::getIntValue, it -> it != null && ((Integer) it) != 0);
	}

	public static <T> PropertyFieldMapper<T, Integer> intField(
		Function<T, Integer> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor, it -> it,
			PropertyFieldMapper::getIntValue, it -> (Integer) it);
	}

	public static <T> PropertyFieldMapper<T, Long>
	longField(Function<T, Long> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor, it -> it,
			PropertyFieldMapper::getLongValue, it -> (Long) it);
	}

	public static <T> PropertyFieldMapper<T, Instant>
	instantField(Function<T, Instant> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor,
			it -> it == null ? null : ((Instant) it).toEpochMilli(),
			PropertyFieldMapper::getLongValue, it -> Instant.ofEpochMilli((long) it));
	}

	public static <T> PropertyFieldMapper<T, Path>
	pathToString(Function<T, Path> propertyAccessor) {
		return stringField(propertyAccessor, it -> it == null ? null : Path.of((String) it));
	}

	public static <T> PropertyFieldMapper<T, Set<String>>
	tagsField(Function<T, Set<String>> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor,
			it -> it, IndexableField::stringValue,
			it -> it == null ? null : Set.of((String) it));
	}

	public static <T, E extends Enum<E>> PropertyFieldMapper<T, Enum<E>>
	enumField(Class<E> enumClass, Function<T, Enum<E>> propertyAccessor) {
		return stringField(propertyAccessor, it -> Enum.valueOf(enumClass, (String) it));
	}

	public Object getIndexedValue(IndexableField indexableField) {
		return indexedValueAccessor.apply(indexableField);
	}

	public P getPropertyValue(T t) {
		return propertyAccessor.apply(t);
	}

	public Object toIndexableValue(Object propertyValue) {
		return toIndexableValue.apply(propertyValue);
	}

	public P toPropertyValue(Object indexedValue) {
		return toPropertyValue.apply(indexedValue);
	}

	private static Integer getIntValue(IndexableField field) {
		return field.storedValue().getIntValue();
	}

	private static Long getLongValue(IndexableField field) {
		return field.storedValue().getLongValue();
	}

	private static String toString(Object s) {
		return s == null ? null : s.toString();
	}

	private static URI toURI(Object value) {
		try {
			return value == null ? null : new URI((String) value);
		} catch (URISyntaxException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
