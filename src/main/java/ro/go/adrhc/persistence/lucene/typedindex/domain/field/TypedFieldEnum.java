package ro.go.adrhc.persistence.lucene.typedindex.domain.field;

import ro.go.adrhc.persistence.lucene.index.domain.field.FieldType;

import java.util.function.Function;

public interface TypedFieldEnum<T> {
	Function<T, Object> accessor();

	String name();

	FieldType fieldType();
}
