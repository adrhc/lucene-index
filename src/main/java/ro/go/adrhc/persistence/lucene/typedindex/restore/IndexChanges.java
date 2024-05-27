package ro.go.adrhc.persistence.lucene.typedindex.restore;

import java.util.Set;

/**
 * @param notIndexedIds           contains the not indexed ids
 * @param indexedButRemovedFromDS contains the indexed ids removed from the data source
 */
public record IndexChanges<ID>(Set<ID> notIndexedIds, Set<ID> indexedButRemovedFromDS) {
	public boolean hasChanges() {
		return !notIndexedIds.isEmpty() || !indexedButRemovedFromDS.isEmpty();
	}

	public int notIndexedSize() {
		return notIndexedIds.size();
	}

	public int indexIdsMissingDataSize() {
		return indexedButRemovedFromDS.size();
	}
}
