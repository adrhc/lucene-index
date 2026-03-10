package ro.go.adrhc.persistence.lucene.operations;

import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexAdder;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexReset;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexUpsert;
import ro.go.adrhc.persistence.lucene.core.typed.write.backup.IndexBackupService;
import ro.go.adrhc.persistence.lucene.operations.merge.IndexMergeService;
import ro.go.adrhc.persistence.lucene.core.typed.write.shallow.TypedIndexShallowUpdater;

import java.io.IOException;
import java.util.Collection;

public interface WriteTypedIndexOperations<T extends Indexable<I, T>, I>
	extends TypedIndexAdder<T>, TypedIndexUpsert<T>, TypedIndexShallowUpdater<I, T>,
	TypedIndexReset<T>, IndexMergeService<T>, IndexBackupService {

	void removeById(I id) throws IOException;

	void removeByIds(Collection<I> ids) throws IOException;

	void removeByQuery(Query query) throws IOException;

	void removeAll() throws IOException;
}
