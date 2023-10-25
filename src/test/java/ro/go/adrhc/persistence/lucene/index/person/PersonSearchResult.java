package ro.go.adrhc.persistence.lucene.index.person;

import ro.go.adrhc.persistence.lucene.index.core.read.ScoreAndDocument;

public record PersonSearchResult(Person person, String oneTokenName) {
	public static PersonSearchResult of(ScoreAndDocument sad) {
		return new PersonSearchResult(Person.of(sad), sad.getField(PersonFields.oneTokenName));
	}
}
