package ro.go.adrhc.persistence.lucene.index.operations;

import ro.go.adrhc.persistence.lucene.index.operations.add.IndexAddService;
import ro.go.adrhc.persistence.lucene.index.operations.merge.IndexMergeService;
import ro.go.adrhc.persistence.lucene.index.operations.remove.IndexRemoveService;
import ro.go.adrhc.persistence.lucene.index.operations.reset.IndexResetService;
import ro.go.adrhc.persistence.lucene.index.operations.restore.IndexShallowUpdateService;
import ro.go.adrhc.persistence.lucene.index.operations.update.IndexUpsertService;
import ro.go.adrhc.persistence.lucene.typedcore.Indexable;

public interface WriteIndexOperations<T extends Indexable<ID, T>, ID>
		extends IndexAddService<T>, IndexUpsertService<T>,
		IndexRemoveService<ID>, IndexShallowUpdateService<ID, T>,
		IndexResetService<T>, IndexMergeService<T> {
}
