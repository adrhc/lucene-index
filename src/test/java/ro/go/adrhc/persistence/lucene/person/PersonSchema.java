package ro.go.adrhc.persistence.lucene.person;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ro.go.adrhc.persistence.lucene.core.bare.field.FieldType;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.field.ObjectLuceneFieldMapper;

import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.core.bare.field.FieldType.*;
import static ro.go.adrhc.persistence.lucene.core.typed.field.ObjectLuceneFieldMapper.*;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum PersonSchema implements LuceneFieldSpec<Person> {
	id(LONG, longField(Person::id)),
	cnp(KEYWORD, Person::cnp),
	nameWord(WORD, Person::name),
	name(PHRASE, Person::name),
	aliasKeyWord(KEYWORD, Person::aliasKeyWord),
	aliasWord(WORD, Person::aliasWord),
	aliasPhrase(PHRASE, Person::aliasPhrase),
	intField(INT, intField(Person::intField)),
	longField(LONG, longField(Person::longField)),
	instantField(LONG, instantField(Person::instantField)),
	storedOnlyField(STORED, Person::storedOnlyField),
	male(INT, booleanField(Person::male)),
	tags(KEYWORD_ARRAY, tagsField(Person::tags));

	private final FieldType fieldType;
	private final ObjectLuceneFieldMapper<Person, ?> fieldSerde;

	PersonSchema(FieldType fieldType, Function<Person, String> propertyAccessor) {
		this.fieldType = fieldType;
		this.fieldSerde = stringField(propertyAccessor);
	}
}
