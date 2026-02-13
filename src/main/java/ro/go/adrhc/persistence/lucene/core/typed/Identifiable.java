package ro.go.adrhc.persistence.lucene.core.typed;

import java.util.Set;

import static ro.go.adrhc.util.collection.SetUtils.mapToSet;

/**
 * Overwrite at least id() or getId()!
 */
public interface Identifiable<ID> {
	static <ID> Set<ID> toIds(
		Iterable<? extends Identifiable<? extends ID>> identifiables) {
		return mapToSet(identifiables, Identifiable::id);
	}

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
