package ro.go.adrhc.persistence.lucene.typedcore.serde;

public interface Identifiable<ID> {
	ID getId();

	default boolean hasId() {
		return getId() != null;
	}
}
