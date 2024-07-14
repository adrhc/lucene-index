package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.typedcore.Indexable;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedAddService;
import ro.go.adrhc.persistence.lucene.typedindex.merge.IndexMergeService;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.reset.TypedResetService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedShallowUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedUpsertService;

import java.io.IOException;
import java.util.Collection;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class WriteIndexOperationsImpl<T extends Indexable<ID, T>, ID>
		implements WriteIndexOperations<T, ID> {
	private final TypedAddService<T> addService;
	private final TypedUpsertService<T> upsertService;
	private final TypedRemoveService<ID> removeService;
	private final TypedResetService<T> resetService;
	private final TypedShallowUpdateService<ID, T> shallowUpdateService;
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
