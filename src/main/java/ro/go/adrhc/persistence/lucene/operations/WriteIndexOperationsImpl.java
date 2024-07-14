package ro.go.adrhc.persistence.lucene.operations;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.operations.add.IndexAddServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.merge.IndexMergeService;
import ro.go.adrhc.persistence.lucene.operations.remove.IndexRemoveServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.reset.IndexResetServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.operations.restore.IndexShallowUpdateServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.update.IndexUpsertServiceImpl;

import java.io.IOException;
import java.util.Collection;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class WriteIndexOperationsImpl<T extends Indexable<ID, T>, ID>
		implements WriteIndexOperations<T, ID> {
	private final IndexAddServiceImpl<T> addService;
	private final IndexUpsertServiceImpl<T> upsertService;
	private final IndexRemoveServiceImpl<ID> removeService;
	private final IndexResetServiceImpl<T> resetService;
	private final IndexShallowUpdateServiceImpl<ID, T> shallowUpdateService;
	private final IndexMergeService<T> mergeService;

	@Override
	public void addMany(Collection<T> tCollection) throws IOException {
		addService.addMany(tCollection);
	}

	@Override
	public void addMany(Stream<T> stream) throws IOException {
		addService.addMany(stream);
	}

	@Override
	public void addOne(T t) throws IOException {
		addService.addOne(t);
	}

	@Override
	public void upsert(T t) throws IOException {
		upsertService.upsert(t);
	}

	@Override
	public void upsertMany(Collection<T> collection) throws IOException {
		upsertService.upsertMany(collection);
	}

	@Override
	public void removeById(ID id) throws IOException {
		removeService.removeById(id);
	}

	@Override
	public void removeByIds(Collection<ID> ids) throws IOException {
		removeService.removeByIds(ids);
	}

	@Override
	public void removeByQuery(Query query) throws IOException {
		removeService.removeByQuery(query);
	}

	@Override
	public void reset(Stream<T> stateAfterReset) throws IOException {
		resetService.reset(stateAfterReset);
	}

	@Override
	public void reset(Iterable<T> stateAfterReset) throws IOException {
		resetService.reset(stateAfterReset);
	}

	@Override
	public void shallowUpdate(IndexDataSource<ID, T> dataSource) throws IOException {
		shallowUpdateService.shallowUpdate(dataSource);
	}

	@Override
	public void shallowUpdateSubset(IndexDataSource<ID, T> dataSource, Query query)
			throws IOException {
		shallowUpdateService.shallowUpdateSubset(dataSource, query);
	}

	@Override
	public void merge(T t) throws IOException {
		mergeService.merge(t);
	}

	@Override
	public void merge(T t, BinaryOperator<T> mergeStrategy) throws IOException {
		mergeService.merge(t, mergeStrategy);
	}

	@Override
	public void mergeMany(Collection<T> tCollection, BinaryOperator<T> mergeStrategy)
			throws IOException {
		mergeService.mergeMany(tCollection, mergeStrategy);
	}

}
