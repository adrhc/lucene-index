package ro.go.adrhc.persistence.lucene.typedindex;

import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.typedindex.add.IndexAddService;
import ro.go.adrhc.persistence.lucene.typedindex.remove.IndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.reset.IndexResetService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.restore.ShallowUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.update.IndexUpsertService;

import java.io.IOException;
import java.util.Collection;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public interface IndexOperations<ID, T extends Indexable<ID, T>>
		extends ReadOnlyIndexOperations<ID, T>, IndexAddService<T>,
		IndexUpsertService<T>, IndexRemoveService<ID>,
		ShallowUpdateService<ID, T>, IndexResetService<T> {
	void addOne(T t) throws IOException;

	void addMany(Collection<T> tCollection) throws IOException;

	void addMany(Stream<T> tStream) throws IOException;

	void upsert(T t) throws IOException;

	void merge(T t) throws IOException;

	/**
	 * @param mergeStrategy 1st param is the stored value while the 2nd is @param t
	 * @param t             might be added (instead of merged) if is not stored yet
	 */
	void merge(T t, BinaryOperator<T> mergeStrategy) throws IOException;

	void upsertAll(Iterable<T> iterable) throws IOException;

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
