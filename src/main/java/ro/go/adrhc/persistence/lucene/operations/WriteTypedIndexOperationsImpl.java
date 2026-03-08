package ro.go.adrhc.persistence.lucene.operations;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.bare.write.DocIndexWriter;
import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexAdder;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemover;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexReset;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexUpsert;
import ro.go.adrhc.persistence.lucene.core.typed.write.shallow.TypedIndexDataSource;
import ro.go.adrhc.persistence.lucene.core.typed.write.shallow.TypedIndexShallowUpdater;
import ro.go.adrhc.persistence.lucene.operations.backup.IndexBackupService;
import ro.go.adrhc.persistence.lucene.operations.merge.IndexMergeService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class WriteTypedIndexOperationsImpl<T extends Indexable<I, T>, I>
	implements WriteTypedIndexOperations<T, I> {
	private final DocIndexWriter indexWriter;
	private final TypedIndexAdder<T> indexAdder;
	private final TypedIndexUpsert<T> indexUpsert;
	private final TypedIndexRemover<I> indexRemover;
	private final TypedIndexReset<T> indexReset;
	private final TypedIndexShallowUpdater<I, T> indexShallowUpdater;
	private final IndexMergeService<T> mergeService;
	private final IndexBackupService backupService;

	@Override
	public void addMany(Iterable<T> tCollection) throws IOException {
		indexAdder.addMany(tCollection);
	}

	@Override
	public void addMany(Stream<T> stream) throws IOException {
		indexAdder.addMany(stream);
	}

	@Override
	public void addOne(T t) throws IOException {
		indexAdder.addOne(t);
	}

	@Override
	public void upsert(T t) throws IOException {
		indexUpsert.upsert(t);
	}

	@Override
	public void upsertMany(Collection<T> collection) throws IOException {
		indexUpsert.upsertMany(collection);
	}

	@Override
	public void removeById(I id) throws IOException {
		indexRemover.removeOne(id);
	}

	@Override
	public void removeByIds(Collection<I> ids) throws IOException {
		indexRemover.removeMany(ids);
	}

	@Override
	public void removeByQuery(Query query) throws IOException {
		indexRemover.removeByQuery(query);
	}

	@Override
	public void removeAll() throws IOException {
		indexRemover.removeAll();
	}

	@Override
	public void reset(Stream<T> stateAfterReset) throws IOException {
		indexReset.reset(stateAfterReset);
	}

	@Override
	public void reset(Iterable<T> stateAfterReset) throws IOException {
		indexReset.reset(stateAfterReset);
	}

	@Override
	public void shallowUpdate(TypedIndexDataSource<I, T> dataSource) throws IOException {
		indexShallowUpdater.shallowUpdate(dataSource);
	}

	@Override
	public void shallowUpdateSubset(TypedIndexDataSource<I, T> dataSource, Query query)
		throws IOException {
		indexShallowUpdater.shallowUpdateSubset(dataSource, query);
	}

	@Override
	public void merge(T t) throws IOException {
		mergeService.merge(t);
	}

	@Override
	public void mergeWithStrategy(T t, BinaryOperator<T> mergeStrategy) throws IOException {
		mergeService.mergeWithStrategy(t, mergeStrategy);
	}

	@Override
	public void mergeMany(Collection<T> tCollection, BinaryOperator<T> mergeStrategy)
		throws IOException {
		mergeService.mergeMany(tCollection, mergeStrategy);
	}

	@Override
	public void backup(Path indexBackupPath) throws IOException {
		backupService.backup(indexBackupPath);
	}

	@Override
	public void commit() throws IOException {
		indexWriter.commit();
	}
}
