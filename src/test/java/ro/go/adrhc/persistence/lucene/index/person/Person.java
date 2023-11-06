package ro.go.adrhc.persistence.lucene.index.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;

public record Person(Long id, String cnp, String name, String aliasKeyword,
		String aliasWord, String aliasPhrase, Integer intField, Long longField,
		String storedOnlyField) implements Identifiable<Long> {
	@Override
	@JsonIgnore
	public Long getId() {
		return id;
	}

	public Person storedOnlyField(String storedOnlyField) {
		return new Person(id, cnp, name, aliasKeyword, aliasWord,
				aliasPhrase, intField, longField, storedOnlyField);
	}
}
