package ro.go.adrhc.persistence.lucene.index.person;

public record PersonSearchResult(Person person, String oneTokenName) {
	public String cnp() {
		return person.cnp();
	}

	public String name() {
		return person.name();
	}

	public String id() {
		return person.id();
	}
}
