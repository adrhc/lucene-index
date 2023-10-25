package ro.go.adrhc.persistence.lucene.index.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ro.go.adrhc.persistence.lucene.typedindex.domain.Identifiable;

public record Person(String id, String cnp, String name) implements Identifiable<String> {
	@JsonIgnore
	@Override
	public String getId() {
		return id;
	}
}
