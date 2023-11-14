package ro.go.adrhc.persistence.lucene.index.person;

import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;

public record Person(Long id, String cnp, String name, String aliasKeyword,
		String aliasWord, String aliasPhrase, Integer intField, Long longField,
		String storedOnlyField) implements Identifiable<Long> {
	public Person storedOnlyField(String storedOnlyField) {
		return new Person(id, cnp, name, aliasKeyword, aliasWord,
				aliasPhrase, intField, longField, storedOnlyField);
	}
}
