package ro.go.adrhc.persistence.lucene.index.person;

import ro.go.adrhc.persistence.lucene.index.core.read.ScoreAndDocument;
import ro.go.adrhc.persistence.lucene.typedindex.domain.Identifiable;

public record Person(String id, String cnp, String name) implements Identifiable<String> {
	public static Person of(ScoreAndDocument sad) {
		return new Person(sad.getField(PersonFields.id),
				sad.getField(PersonFields.cnp), sad.getField(PersonFields.name));
	}

	@Override
	public String getId() {
		return id;
	}
}
