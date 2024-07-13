package ro.go.adrhc.persistence.lucene.typedcore.field;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexableField;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Set;
import java.util.function.Function;

import static ro.go.adrhc.util.text.StringUtils.concat;

@Slf4j
public record TypedFieldSerde<T, P>(Function<T, P> propertyAccessor,
		Function<Object, ?> toFieldValue,
		Function<IndexableField, Object> fieldAccessor,
		Function<Object, P> toPropertyValue) {
	private static final Function<IndexableField, Object> INT_FIELD_ACCESSOR
			= field -> field.storedValue().getIntValue();
	private static final Function<IndexableField, Object> LONG_FIELD_ACCESSOR
			= field -> field.storedValue().getLongValue();

	public static <T, P> TypedFieldSerde<T, P> stringField(
			Function<T, P> propertyAccessor,
			Function<Object, P> indexedValueConverter) {
		return new TypedFieldSerde<>(propertyAccessor,
				TypedFieldSerde::toString,
				IndexableField::stringValue, indexedValueConverter);
	}

	public static <T> TypedFieldSerde<T, String> stringField(
			Function<T, String> propertyAccessor) {
		return new TypedFieldSerde<>(propertyAccessor,
				it -> it, IndexableField::stringValue, it -> (String) it);
	}

	public static <T> TypedFieldSerde<T, URI> uriField(Function<T, URI> propertyAccessor) {
		return new TypedFieldSerde<>(propertyAccessor, TypedFieldSerde::toString,
				IndexableField::stringValue, TypedFieldSerde::toURI);
	}

	public static <T> TypedFieldSerde<T, Integer> intField(Function<T, Integer> propertyAccessor) {
		return new TypedFieldSerde<>(propertyAccessor,
				it -> it, INT_FIELD_ACCESSOR, it -> (Integer) it);
	}

	public static <T> TypedFieldSerde<T, Boolean> booleanField(
			Function<T, Boolean> propertyAccessor) {
		return new TypedFieldSerde<>(propertyAccessor,
				it -> it != null && ((Boolean) it) ? 1 : 0,
				INT_FIELD_ACCESSOR, it -> it != null && ((Integer) it) != 0);
	}

	public static <T> TypedFieldSerde<T, Long>
	longField(Function<T, Long> propertyAccessor) {
		return new TypedFieldSerde<>(propertyAccessor,
				it -> it, LONG_FIELD_ACCESSOR, it -> (Long) it);
	}

	public static <T> TypedFieldSerde<T, Instant>
	instantField(Function<T, Instant> propertyAccessor) {
		return new TypedFieldSerde<>(propertyAccessor,
				it -> it == null ? null : ((Instant) it).toEpochMilli(),
				LONG_FIELD_ACCESSOR, it -> Instant.ofEpochMilli((long) it));
	}

	public static <T> TypedFieldSerde<T, Path>
	pathToString(Function<T, Path> propertyAccessor) {
		return stringField(propertyAccessor, it -> it == null ? null : Path.of((String) it));
	}

	public static <T> TypedFieldSerde<T, Set<String>>
	tagsField(Function<T, Set<String>> propertyAccessor) {
		return new TypedFieldSerde<>(propertyAccessor,
				it -> stringSet(it).isEmpty() ? null : concat(" ", stringSet(it)),
				IndexableField::stringValue,
				it -> it == null ? null : Set.of(((String) it).split("\\s+")));
	}

	public static <T, E extends Enum<E>> TypedFieldSerde<T, Enum<E>>
	enumField(Class<E> enumClass, Function<T, Enum<E>> propertyAccessor) {
		return stringField(propertyAccessor,
				it -> Enum.valueOf(enumClass, (String) it));
	}

	private static Set<String> stringSet(Object o) {
		return (Set<String>) o;
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
