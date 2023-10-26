package ro.go.adrhc.persistence.lucene.typedindex.domain.field.spec;

import ro.go.adrhc.persistence.lucene.index.domain.field.FieldType;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedFieldEnum;

import java.util.function.Function;

public record TypedFieldSpec<T>(FieldType type, Enum<?> field, Function<T, ?> valueAccessor) {
	public static <T, E extends Enum<E> & TypedFieldEnum<T>> TypedFieldSpec<T> of(E typedFieldEnum) {
		return new TypedFieldSpec<>(typedFieldEnum.fieldType(), typedFieldEnum, typedFieldEnum.accessor());
	}

	public Object value(T t) {
		return valueAccessor.apply(t);
	}
}
