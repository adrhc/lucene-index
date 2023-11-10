package ro.go.adrhc.persistence.lucene.typedindex.domain;

public interface Identifiable<ID> {
	ID getId();

	default boolean hasId() {
		return getId() != null;
	}
}