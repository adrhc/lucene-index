package ro.go.adrhc.persistence.lucene.typedcore.field;

import org.apache.lucene.index.IndexableField;

import java.nio.file.Path;
import java.util.function.Function;

public record TypedFieldSerde<T>(Function<T, ?> typedAccessor, Function<Object, ?> toFieldValue,
		Function<IndexableField, Object> indexableFieldAccessor, Function<Object, ?> toTypedValue) {
	private static final Function<IndexableField, Object> INT_FIELD_ACCESSOR
			= field -> field.storedValue().getIntValue();
	private static final Function<IndexableField, Object> LONG_FIELD_ACCESSOR
			= field -> field.storedValue().getLongValue();

	public static <T> TypedFieldSerde<T> stringFieldSerde(
			Function<T, ?> typedAccessor, Function<Object, ?> toTypedValue) {
		return new TypedFieldSerde<>(typedAccessor, Object::toString,
				IndexableField::stringValue, toTypedValue);
	}

	public static <T> TypedFieldSerde<T> stringFieldSerde(Function<T, String> typedAccessor) {
		return new TypedFieldSerde<>(typedAccessor,
				it -> it, IndexableField::stringValue, it -> it);
	}

	public static <T> TypedFieldSerde<T> intFieldSerde(Function<T, Integer> typedAccessor) {
		return new TypedFieldSerde<>(typedAccessor, it -> it, INT_FIELD_ACCESSOR, it -> it);
	}

	public static <T> TypedFieldSerde<T> longFieldSerde(Function<T, Long> typedAccessor) {
		return new TypedFieldSerde<>(typedAccessor, it -> it, LONG_FIELD_ACCESSOR, it -> it);
	}

	public static <T> TypedFieldSerde<T> pathFieldSerde(Function<T, Path> typedAccessor) {
		return stringFieldSerde(typedAccessor, it -> Path.of((String) it));
	}
}
