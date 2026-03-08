package ro.go.adrhc.persistence.lucene.operations;

import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexAdder;
import ro.go.adrhc.persistence.lucene.operations.backup.IndexBackupService;
import ro.go.adrhc.persistence.lucene.operations.merge.IndexMergeService;
import ro.go.adrhc.persistence.lucene.operations.reset.IndexResetService;
import ro.go.adrhc.persistence.lucene.operations.restore.IndexShallowUpdateService;
import ro.go.adrhc.persistence.lucene.operations.update.IndexUpsertService;

import java.io.IOException;
import java.util.Collection;

public interface WriteIndexOperations<T extends Indexable<I, T>, I>
	extends TypedIndexAdder<T>, IndexUpsertService<T>, IndexShallowUpdateService<I, T>,
	IndexResetService<T>, IndexMergeService<T>, IndexBackupService {

	void removeById(I id) throws IOException;

	void removeByIds(Collection<I> ids) throws IOException;

	void removeByQuery(Query query) throws IOException;

	void removeAll() throws IOException;
}
