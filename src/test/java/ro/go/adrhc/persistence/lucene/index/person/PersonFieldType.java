package ro.go.adrhc.persistence.lucene.index.person;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.lucene.index.IndexableField;
import ro.go.adrhc.persistence.lucene.core.field.FieldType;
import ro.go.adrhc.persistence.lucene.core.queries.FieldQueries;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.core.field.FieldType.*;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum PersonFieldType implements TypedField<Person> {
	id(LONG, Person::id, LONG_FIELD_ACCESSOR, true),
	cnp(KEYWORD, Person::cnp),
	nameWord(WORD, Person::name),
	name(PHRASE, Person::name),
	aliasKeyWord(KEYWORD, Person::aliasKeyword),
	aliasWord(WORD, Person::aliasWord),
	aliasPhrase(PHRASE, Person::aliasPhrase),
	intField(INT, Person::intField), // int can't be stored but only indexed!
	longField(LONG, Person::longField, LONG_FIELD_ACCESSOR),
	storedOnlyField(STORED, Person::storedOnlyField);

	public static final FieldQueries NAME_WORD_QUERIES = FieldQueries.create(PersonFieldType.nameWord);
	public static final FieldQueries NAME_QUERIES = FieldQueries.create(PersonFieldType.name);
	public static final FieldQueries ALIAS_KEYWORD_QUERIES = FieldQueries.create(PersonFieldType.aliasKeyWord);
	public static final FieldQueries ALIAS_WORD_QUERIES = FieldQueries.create(PersonFieldType.aliasWord);
	public static final FieldQueries ALIAS_PHRASE_QUERIES = FieldQueries.create(PersonFieldType.aliasPhrase);
	public static final FieldQueries CNP_QUERIES = FieldQueries.create(PersonFieldType.cnp);
	public static final FieldQueries ID_QUERIES = FieldQueries.create(PersonFieldType.id);

	private final FieldType fieldType;
	private final Function<Person, ?> accessor;
	private final Function<IndexableField, Object> fieldValueAccessor;
	private final boolean isIdField;

	PersonFieldType(FieldType fieldType, Function<Person, ?> accessor) {
		this.fieldType = fieldType;
		this.accessor = accessor;
		this.fieldValueAccessor = STRING_FIELD_ACCESSOR;
		this.isIdField = false;
	}

	PersonFieldType(FieldType fieldType, Function<Person, ?> accessor,
			Function<IndexableField, Object> fieldValueAccessor) {
		this.fieldType = fieldType;
		this.accessor = accessor;
		this.fieldValueAccessor = fieldValueAccessor;
		this.isIdField = false;
	}
}
