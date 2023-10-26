package ro.go.adrhc.persistence.lucene.typedindex.domain.field.spec;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.index.domain.field.FieldType.*;

@RequiredArgsConstructor
public class TypedFieldSpecFactory {
	public static <T> TypedFieldSpec<T> identifier(
			Enum<?> field, Function<T, Object> valueAccessor) {
		return new TypedFieldSpec<T>(IDENTIFIER, field, valueAccessor);
	}

	public static <T> TypedFieldSpec<T> word(
			Enum<?> field, Function<T, Object> valueAccessor) {
		return new TypedFieldSpec<T>(WORD, field, valueAccessor);
	}

	public static <T> TypedFieldSpec<T> phrase(
			Enum<?> field, Function<T, Object> valueAccessor) {
		return new TypedFieldSpec<T>(PHRASE, field, valueAccessor);
	}
}
