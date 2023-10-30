package ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds;

public interface Identifiable<ID> {
	ID getId();

	default boolean hasId() {
		return getId() != null;
	}
}
