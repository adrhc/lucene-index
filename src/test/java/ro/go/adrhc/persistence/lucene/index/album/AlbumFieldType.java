package ro.go.adrhc.persistence.lucene.index.album;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.lucene.index.IndexableField;
import ro.go.adrhc.persistence.lucene.index.domain.field.FieldType;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.index.domain.field.FieldType.*;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum AlbumFieldType implements TypedField<Album> {
	id(KEYWORD, Identifiable::getId, STRING_FIELD_ACCESSOR, true),
	name(PHRASE, Album::name),
	storedOnlyField(STORED, Album::storedOnlyField);

	private final FieldType fieldType;
	private final Function<Album, ?> accessor;
	private final Function<IndexableField, Object> fieldValueAccessor;
	private final boolean isIdField;

	AlbumFieldType(FieldType fieldType, Function<Album, ?> accessor) {
		this.fieldType = fieldType;
		this.accessor = accessor;
		this.fieldValueAccessor = STRING_FIELD_ACCESSOR;
		this.isIdField = false;
	}

	AlbumFieldType(FieldType fieldType, Function<Album, ?> accessor,
			Function<IndexableField, Object> fieldValueAccessor) {
		this.fieldType = fieldType;
		this.accessor = accessor;
		this.fieldValueAccessor = fieldValueAccessor;
		this.isIdField = false;
	}
}