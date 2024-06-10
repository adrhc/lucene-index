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
public record TypedFieldSerde<T>(Function<T, ?> typedAccessor,
		Function<Object, ?> toFieldValue,
		Function<IndexableField, Object> indexableFieldAccessor,
		Function<Object, ?> toTypedValue) {
	private static final Function<IndexableField, Object> INT_FIELD_ACCESSOR
			= field -> field.storedValue().getIntValue();
	private static final Function<IndexableField, Object> LONG_FIELD_ACCESSOR
			= field -> field.storedValue().getLongValue();

	public static <T> TypedFieldSerde<T> stringField(
			Function<T, ?> typedAccessor,
			Function<Object, ?> indexedValueConverter) {
		return new TypedFieldSerde<>(typedAccessor,
				TypedFieldSerde::toString,
				IndexableField::stringValue, indexedValueConverter);
	}

	public static <T> TypedFieldSerde<T> stringField(Function<T, String> typedAccessor) {
		return new TypedFieldSerde<>(typedAccessor,
				it -> it, IndexableField::stringValue, it -> it);
	}

	public static <T> TypedFieldSerde<T> uriField(Function<T, URI> typedAccessor) {
		return new TypedFieldSerde<>(typedAccessor, TypedFieldSerde::toString,
				IndexableField::stringValue, TypedFieldSerde::toURI);
	}

	public static <T> TypedFieldSerde<T> intField(Function<T, Integer> typedAccessor) {
		return new TypedFieldSerde<>(typedAccessor, it -> it, INT_FIELD_ACCESSOR, it -> it);
	}

	public static <T> TypedFieldSerde<T> booleanField(Function<T, Boolean> typedAccessor) {
		return new TypedFieldSerde<>(typedAccessor,
				it -> it != null && ((Boolean) it) ? 1 : 0,
				INT_FIELD_ACCESSOR, it -> it != null && ((Integer) it) != 0);
	}

	public static <T> TypedFieldSerde<T> longField(Function<T, Long> typedAccessor) {
		return new TypedFieldSerde<>(typedAccessor, it -> it, LONG_FIELD_ACCESSOR, it -> it);
	}

	public static <T> TypedFieldSerde<T> instantField(Function<T, Instant> typedAccessor) {
		return new TypedFieldSerde<>(typedAccessor,
				it -> it == null ? null : ((Instant) it).toEpochMilli(),
				LONG_FIELD_ACCESSOR, it -> Instant.ofEpochMilli((long) it));
	}

	public static <T> TypedFieldSerde<T> pathToString(Function<T, Path> typedAccessor) {
		return stringField(typedAccessor, it -> it == null ? null : Path.of((String) it));
	}

	public static <T> TypedFieldSerde<T> tagsField(Function<T, Set<String>> typedAccessor) {
		return new TypedFieldSerde<>(typedAccessor,
				it -> textSet(it).isEmpty() ? null : concat(" ", textSet(it)),
				IndexableField::stringValue,
				it -> it == null ? null : Set.of(((String) it).split("\\s+")));
	}

	public static <T, E extends Enum<E>> TypedFieldSerde<T>
	enumField(Class<E> enumClass, Function<T, Enum<?>> typedAccessor) {
		return stringField(typedAccessor,
				it -> Enum.valueOf(enumClass, (String) it));
	}

	private static Set<String> textSet(Object o) {
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
