package ro.go.adrhc.persistence.lucene.index.person;

import ro.go.adrhc.persistence.lucene.typedindex.Indexable;

import java.time.Instant;

public record Person(Long id, String cnp, String name, String aliasKeyWord,
		String aliasWord, String aliasPhrase, Integer intField, Long longField,
		Instant instantField, String storedOnlyField, boolean male)
		implements Indexable<Long, Person> {
	public Person storedOnlyField(String storedOnlyField) {
		return new Person(id, cnp, name, aliasKeyWord, aliasWord, aliasPhrase,
				intField, longField, Instant.now(), storedOnlyField, male);
	}

	@Override
	public Person merge(Person another) {
		return another;
	}
}
