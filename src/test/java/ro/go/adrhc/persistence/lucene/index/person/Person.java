package ro.go.adrhc.persistence.lucene.index.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;

public record Person(String id, Integer km, String cnp, String name, String aliasKeyword, String aliasWord,
		String aliasPhrase) implements Identifiable<String> {
	@JsonIgnore
	@Override
	public String getId() {
		return id;
	}
}
