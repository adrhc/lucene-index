package ro.go.adrhc.persistence.lucene.typedindex;

import ro.go.adrhc.persistence.lucene.typedcore.Indexable;
import ro.go.adrhc.persistence.lucene.typedindex.add.IndexAddService;
import ro.go.adrhc.persistence.lucene.typedindex.merge.IndexMergeService;
import ro.go.adrhc.persistence.lucene.typedindex.remove.IndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.reset.IndexResetService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexShallowUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.update.IndexUpsertService;

public interface WriteIndexOperations<T extends Indexable<ID, T>, ID>
		extends IndexAddService<T>, IndexUpsertService<T>,
		IndexRemoveService<ID>, IndexShallowUpdateService<ID, T>,
		IndexResetService<T>, IndexMergeService<T> {
}
