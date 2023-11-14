package ro.go.adrhc.persistence.lucene.index.album;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ro.go.adrhc.persistence.lucene.core.field.FieldType;
import ro.go.adrhc.persistence.lucene.core.query.FieldQueries;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedFieldSerde;

import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.core.field.FieldType.*;
import static ro.go.adrhc.persistence.lucene.typedcore.field.TypedFieldSerde.pathToStringIndexableField;
import static ro.go.adrhc.persistence.lucene.typedcore.field.TypedFieldSerde.stringIndexableField;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum AlbumFieldType implements TypedField<Album> {
	id(KEYWORD, true, pathToStringIndexableField(Album::id)),
	name(PHRASE, Album::name),
	storedOnlyField(STORED, Album::storedOnlyField);

	public static final FieldQueries NAME_QUERIES = FieldQueries.create(AlbumFieldType.name);
	public static final FieldQueries ID_QUERIES = FieldQueries.create(AlbumFieldType.id);

	private final FieldType fieldType;
	private final boolean isIdField;
	private final TypedFieldSerde<Album> fieldSerde;

	AlbumFieldType(FieldType fieldType, Function<Album, String> typedAccessor) {
		this.fieldType = fieldType;
		this.isIdField = false;
		this.fieldSerde = stringIndexableField(typedAccessor);
	}
}
