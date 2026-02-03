package ro.go.adrhc.persistence.lucene.person;

import ro.go.adrhc.persistence.lucene.core.typed.Indexable;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public record Person(Long id, String cnp, String name, String aliasKeyWord,
	String aliasWord, String aliasPhrase, Integer intField, Long longField,
	Instant instantField, String storedOnlyField, boolean male, Set<String> tags)
	implements Indexable<Long, Person> {
	public static Person addTags(Person person, String... tags) {
		person.addTags(Set.of(tags));
		return person;
	}

	public Person storedOnlyField(String storedOnlyField) {
		return new Person(id, cnp, name, aliasKeyWord, aliasWord, aliasPhrase,
			intField, longField, Instant.now(), storedOnlyField, male, new HashSet<>());
	}

	public void addTags(Set<String> tags) {
		this.tags.addAll(tags);
	}

	@Override
	public Person merge(Person another) {
		return another;
	}
}
