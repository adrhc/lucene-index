package ro.go.adrhc.persistence.lucene.index.person;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ro.go.adrhc.persistence.lucene.index.domain.field.FieldType;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedFieldEnum;

import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.index.domain.field.FieldType.*;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum PersonFieldType implements TypedFieldEnum<Person> {
	id(KEYWORD, Person::id),
	name(PHRASE, Person::name),
	nameAsWord(WORD, Person::name),
	cnp(KEYWORD, Person::cnp),
	km(LONG, Person::km);

	private final FieldType fieldType;
	private final Function<Person, Object> accessor;
}
