package ro.go.adrhc.persistence.lucene.album;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ro.go.adrhc.persistence.lucene.core.bare.field.FieldType;
import ro.go.adrhc.persistence.lucene.core.bare.query.FieldQueries;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.field.ObjectLuceneFieldMapper;

import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.core.bare.field.FieldType.*;
import static ro.go.adrhc.persistence.lucene.core.typed.field.ObjectLuceneFieldMapper.pathToString;
import static ro.go.adrhc.persistence.lucene.core.typed.field.ObjectLuceneFieldMapper.stringField;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum AlbumFieldType implements LuceneFieldSpec<Album> {
	id(KEYWORD, pathToString(Album::id), true),
	name(PHRASE, Album::name),
	storedOnlyField(STORED, Album::storedOnlyField);

	public static final FieldQueries NAME_QUERIES = FieldQueries.create(AlbumFieldType.name);
	public static final FieldQueries ID_QUERIES = FieldQueries.create(AlbumFieldType.id);

	private final FieldType fieldType;
	private final ObjectLuceneFieldMapper<Album, ?> fieldSerde;
	private final boolean isIdField;

	AlbumFieldType(FieldType fieldType, Function<Album, String> typedAccessor) {
		this.fieldType = fieldType;
		this.isIdField = false;
		this.fieldSerde = stringField(typedAccessor);
	}
}
