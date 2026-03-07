package ro.go.adrhc.persistence.lucene.album;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ro.go.adrhc.persistence.lucene.core.bare.field.FieldType;
import ro.go.adrhc.persistence.lucene.core.bare.query.FieldQueries;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.field.PropertyFieldMapper;

import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.core.bare.field.FieldType.*;
import static ro.go.adrhc.persistence.lucene.core.typed.field.PropertyFieldMapperFactory.pathMapper;
import static ro.go.adrhc.persistence.lucene.core.typed.field.PropertyFieldMapperFactory.stringMapper;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum AlbumSchema implements LuceneFieldSpec<Album> {
	id(KEYWORD, pathMapper(Album::id)),
	name(TEXT, Album::name),
	storedOnlyField(STORED, Album::storedOnlyField);

	public static final FieldQueries NAME_QUERIES = FieldQueries.create(AlbumSchema.name);
	public static final FieldQueries ID_QUERIES = FieldQueries.create(AlbumSchema.id);

	private final FieldType fieldType;
	private final PropertyFieldMapper<Album, ?, ?, ?> fieldSerde;

	AlbumSchema(FieldType fieldType, Function<Album, String> typedAccessor) {
		this.fieldType = fieldType;
		this.fieldSerde = stringMapper(typedAccessor);
	}

	@Override
	public boolean isSortable() {
		return fieldType() == WORD;
	}
}
