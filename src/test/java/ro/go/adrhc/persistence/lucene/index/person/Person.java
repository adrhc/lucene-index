package ro.go.adrhc.persistence.lucene.index.person;

import ro.go.adrhc.persistence.lucene.index.core.read.ScoreAndDocument;

public record Person(String id, String name, String cnp) {
	public static Person of(ScoreAndDocument sad) {
		return new Person(sad.getField(PersonFields.id),
				sad.getField(PersonFields.name), sad.getField(PersonFields.cnp));
	}
}
