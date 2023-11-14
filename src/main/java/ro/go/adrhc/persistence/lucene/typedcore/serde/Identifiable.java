package ro.go.adrhc.persistence.lucene.typedcore.serde;

/**
 * Overwrite at least id() or getId()!
 */
public interface Identifiable<ID> {
	default ID id() {
		return getId();
	}

	default ID getId() {
		return id();
	}

	default boolean hasId() {
		return getId() != null;
	}
}
