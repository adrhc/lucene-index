package ro.go.adrhc.persistence.lucene.typedindex;

import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.typedcore.Indexable;
import ro.go.adrhc.persistence.lucene.typedindex.add.IndexAddService;
import ro.go.adrhc.persistence.lucene.typedindex.merge.IndexMergeService;
import ro.go.adrhc.persistence.lucene.typedindex.remove.IndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.reset.IndexResetService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.restore.ShallowUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.update.IndexUpsertService;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * This is a facade for all index services.
 */
public interface IndexOperations<ID, T extends Indexable<ID, T>>
		extends ReadOnlyIndexOperations<ID, T>, IndexAddService<T>,
		IndexUpsertService<T>, IndexRemoveService<ID>,
		ShallowUpdateService<ID, T>, IndexResetService<T>,
		IndexMergeService<T> {
	void addOne(T t) throws IOException;

	void addMany(Collection<T> tCollection) throws IOException;

	void addMany(Stream<T> tStream) throws IOException;

	void upsert(T t) throws IOException;

	void upsertMany(Collection<T> tCollection) throws IOException;

	void removeByIds(Collection<ID> ids) throws IOException;

	void removeById(ID id) throws IOException;

	void removeByQuery(Query query) throws IOException;

	/**
	 * remove the index content then set it to tIterable
	 */
	void reset(Iterable<T> tIterable) throws IOException;

	void reset(Stream<T> tStream) throws IOException;

	void shallowUpdate(IndexDataSource<ID, T> dataSource) throws IOException;

	void shallowUpdateSubset(IndexDataSource<ID, T> dataSource, Query query) throws IOException;
}
