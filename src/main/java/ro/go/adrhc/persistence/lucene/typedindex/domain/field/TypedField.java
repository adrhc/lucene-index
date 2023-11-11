package ro.go.adrhc.persistence.lucene.typedindex.domain.field;

import org.apache.lucene.index.IndexableField;
import ro.go.adrhc.persistence.lucene.core.field.FieldType;
import ro.go.adrhc.util.Assert;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Function;

public interface TypedField<T> {
	Function<IndexableField, Object> STRING_FIELD_ACCESSOR = IndexableField::stringValue;
	Function<IndexableField, Object> LONG_FIELD_ACCESSOR = field -> field.storedValue().getLongValue();

	static <E extends Enum<E> & TypedField<?>> E getIdField(Class<E> enumClass) {
		Optional<E> id = EnumSet.allOf(enumClass).stream().filter(TypedField::isIdField).findAny();
		Assert.isTrue(id.isPresent(), enumClass + " must have an id field!");
		return id.get();
	}

	Function<T, ?> accessor();

	Function<IndexableField, Object> fieldValueAccessor();

	String name();

	boolean isIdField();

	FieldType fieldType();
}
