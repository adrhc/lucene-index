package ro.go.adrhc.persistence.lucene.index.person;

import ro.go.adrhc.persistence.lucene.typedindex.domain.Identifiable;

public record Person(String id, String cnp, String name) implements Identifiable<String> {
	@Override
	public String getId() {
		return id;
	}
}
