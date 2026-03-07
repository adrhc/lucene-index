package ro.go.adrhc.persistence.lucene.person;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ro.go.adrhc.persistence.lucene.core.bare.field.FieldType;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.field.PropertyFieldMapper;

import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.core.bare.field.FieldType.*;
import static ro.go.adrhc.persistence.lucene.core.typed.field.PropertyFieldMapperFactory.*;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum PersonSchema implements LuceneFieldSpec<Person> {
	id(LONG, longMapper(Person::id)),
	cnp(KEYWORD, Person::cnp),
	name(TEXT, Person::name),
	nameWord(WORD, Person::name),
	aliasKeyWord(KEYWORD, Person::aliasKeyWord),
	aliasWord(WORD, Person::aliasWord),
	aliasPhrase(TEXT, Person::aliasPhrase),
	intField(INT, intMapper(Person::intField)),
	longField(LONG, longMapper(Person::longField)),
	instantField(LONG, instantMapper(Person::instantField)),
	storedOnlyField(STORED, Person::storedOnlyField),
	male(INT, booleanMapper(Person::male)),
	tags(KEYWORD_ARRAY, stringSetMapper(Person::tags));

	private final FieldType fieldType;
	private final PropertyFieldMapper<Person, ?, ?, ?> fieldSerde;

	PersonSchema(FieldType fieldType, Function<Person, String> propertyAccessor) {
		this.fieldType = fieldType;
		this.fieldSerde = stringMapper(propertyAccessor);
	}

	@Override
	public boolean isSortable() {
		return fieldType == KEYWORD || fieldType == WORD ||
			fieldType == INT || fieldType == LONG || fieldType == STORED;
	}
}
