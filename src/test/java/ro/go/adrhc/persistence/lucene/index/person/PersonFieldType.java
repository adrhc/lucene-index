package ro.go.adrhc.persistence.lucene.index.person;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ro.go.adrhc.persistence.lucene.core.field.FieldType;
import ro.go.adrhc.persistence.lucene.core.query.FieldQueries;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedFieldSerde;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;

import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.core.field.FieldType.*;
import static ro.go.adrhc.persistence.lucene.typedcore.field.TypedFieldSerde.*;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum PersonFieldType implements TypedField<Person> {
    id(LONG, longField(Identifiable::id), true),
    cnp(KEYWORD, Person::cnp),
    nameWord(WORD, Person::name),
    name(PHRASE, Person::name),
    aliasKeyWord(KEYWORD, Person::aliasKeyword),
    aliasWord(WORD, Person::aliasWord),
    aliasPhrase(PHRASE, Person::aliasPhrase),
    intField(INT, intField(Person::intField), false),
    longField(LONG, longField(Person::longField), false),
    instantField(LONG, instantField(Person::instantField), false),
    storedOnlyField(STORED, Person::storedOnlyField);

    public static final FieldQueries NAME_WORD_QUERIES = FieldQueries.create(PersonFieldType.nameWord);
    public static final FieldQueries NAME_QUERIES = FieldQueries.create(PersonFieldType.name);
    public static final FieldQueries ALIAS_KEYWORD_QUERIES = FieldQueries.create(PersonFieldType.aliasKeyWord);
    public static final FieldQueries ALIAS_WORD_QUERIES = FieldQueries.create(PersonFieldType.aliasWord);
    public static final FieldQueries ALIAS_PHRASE_QUERIES = FieldQueries.create(PersonFieldType.aliasPhrase);
    public static final FieldQueries CNP_QUERIES = FieldQueries.create(PersonFieldType.cnp);
    public static final FieldQueries ID_QUERIES = FieldQueries.create(PersonFieldType.id);

    private final FieldType fieldType;
    private final TypedFieldSerde<Person> fieldSerde;
    private final boolean isIdField;

    PersonFieldType(FieldType fieldType, Function<Person, String> typedAccessor) {
        this.fieldType = fieldType;
        this.isIdField = false;
        this.fieldSerde = stringField(typedAccessor);
    }
}
