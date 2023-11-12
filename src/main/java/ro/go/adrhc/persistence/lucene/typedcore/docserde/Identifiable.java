package ro.go.adrhc.persistence.lucene.typedcore.docserde;

public interface Identifiable<ID> {
	ID getId();

	default boolean hasId() {
		return getId() != null;
	}
}
