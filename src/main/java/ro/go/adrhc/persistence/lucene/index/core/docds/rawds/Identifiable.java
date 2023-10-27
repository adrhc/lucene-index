package ro.go.adrhc.persistence.lucene.index.core.docds.rawds;

public interface Identifiable<ID> {
	ID getId();

	default boolean hasId() {
		return getId() != null;
	}
}
