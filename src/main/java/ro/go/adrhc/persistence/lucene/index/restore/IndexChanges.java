package ro.go.adrhc.persistence.lucene.index.restore;

import java.util.Set;

/**
 * @param notIndexedIds      contains not indexed data identifiers
 * @param obsoleteIndexedIds contains indexed ids for which the indexed data no longer exist
 */
public record IndexChanges(Set<String> notIndexedIds, Set<String> obsoleteIndexedIds) {
	public boolean hasChanges() {
		return !notIndexedIds.isEmpty() || !obsoleteIndexedIds.isEmpty();
	}

	public int notIndexedSize() {
		return notIndexedIds.size();
	}

	public int indexIdsMissingDataSize() {
		return obsoleteIndexedIds.size();
	}
}
