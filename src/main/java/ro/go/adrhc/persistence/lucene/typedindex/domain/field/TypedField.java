package ro.go.adrhc.persistence.lucene.typedindex.domain.field;

import ro.go.adrhc.persistence.lucene.index.domain.field.FieldType;
import ro.go.adrhc.util.Assert;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Function;

public interface TypedField<T> {
	static <T, E extends Enum<E> & TypedField<T>> E getIdField(Class<E> enumClass) {
		Optional<E> id = EnumSet.allOf(enumClass).stream().filter(TypedField::isIdField).findAny();
		Assert.isTrue(id.isPresent(), enumClass + " must have an id field!");
		return id.get();
	}

	Function<T, Object> accessor();

	String name();

	boolean isIdField();

	FieldType fieldType();
}