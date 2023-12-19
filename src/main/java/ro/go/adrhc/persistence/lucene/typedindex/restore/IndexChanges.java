package ro.go.adrhc.persistence.lucene.typedindex.restore;

import java.util.Set;

/**
 * @param notIndexedIds      contains not indexed data identifiers
 * @param obsoleteIndexedIds contains indexed ids for which the indexed data no longer exist
 */
public record IndexChanges<ID>(Set<ID> notIndexedIds, Set<ID> obsoleteIndexedIds) {
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
