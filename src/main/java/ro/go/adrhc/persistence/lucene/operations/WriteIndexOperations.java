package ro.go.adrhc.persistence.lucene.operations;

import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexAdder;
import ro.go.adrhc.persistence.lucene.operations.backup.IndexBackupService;
import ro.go.adrhc.persistence.lucene.operations.merge.IndexMergeService;
import ro.go.adrhc.persistence.lucene.operations.remove.IndexRemoveService;
import ro.go.adrhc.persistence.lucene.operations.reset.IndexResetService;
import ro.go.adrhc.persistence.lucene.operations.restore.IndexShallowUpdateService;
import ro.go.adrhc.persistence.lucene.operations.update.IndexUpsertService;

public interface WriteIndexOperations<T extends Indexable<ID, T>, ID>
	extends TypedIndexAdder<T>, IndexUpsertService<T>,
	IndexRemoveService<ID>, IndexShallowUpdateService<ID, T>,
	IndexResetService<T>, IndexMergeService<T>, IndexBackupService {
}
