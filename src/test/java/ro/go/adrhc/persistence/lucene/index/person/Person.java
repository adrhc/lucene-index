package ro.go.adrhc.persistence.lucene.index.person;

import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;

public record Person(int id, Long km, String cnp, String name, String aliasKeyword,
		String aliasWord, String aliasPhrase, String misc) implements Identifiable<Integer> {
	@Override
	public Integer getId() {
		return id;
	}

	public Person misc(String misc) {
		return new Person(id, km, cnp, name, aliasKeyword, aliasWord, aliasPhrase, misc);
	}
}
