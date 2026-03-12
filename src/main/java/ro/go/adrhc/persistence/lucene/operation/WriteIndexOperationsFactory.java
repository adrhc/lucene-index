package ro.go.adrhc.persistence.lucene.operation;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.bare.write.DocIndexWriter;
import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.core.typed.read.IndexReadService;
import ro.go.adrhc.persistence.lucene.core.typed.read.IndexReadServiceImpl;
import ro.go.adrhc.persistence.lucene.core.typed.write.*;
import ro.go.adrhc.persistence.lucene.service.backup.IndexBackupService;
import ro.go.adrhc.persistence.lucene.service.backup.IndexBackupServiceImpl;
import ro.go.adrhc.persistence.lucene.service.shallow.TypedIndexShallowUpdater;
import ro.go.adrhc.persistence.lucene.service.shallow.TypedIndexShallowUpdaterImpl;
import ro.go.adrhc.persistence.lucene.service.merge.IndexMergeService;
import ro.go.adrhc.persistence.lucene.service.merge.IndexMergeServiceImpl;

@RequiredArgsConstructor
public class WriteIndexOperationsFactory<T extends Indexable<I, T>, I> {
	private final DocIndexWriter indexWriter;
	private final TypedIndexAdder<T> indexAdder;
	private final TypedIndexUpsert<T> indexUpsert;
	private final TypedIndexRemover<I> indexRemover;
	private final IndexMergeService<T> mergeService;
	private final TypedIndexReset<T> indexReset;
	private final TypedIndexShallowUpdater<I, T> shallowUpdateService;
	private final IndexBackupService backupService;

	public static <T extends Indexable<I, T>, I>
	WriteIndexOperationsFactory<T, I> of(IndexOperationsParams<T> params) {
		IndexReadService<I, T> retrieveService = createRetrieveService(params);
		IndexBackupService backupService = createBackupService(params);
		DocIndexWriter indexWriter = new DocIndexWriter(params.indexWriter());
		TypedIndexAdder<T> typedIndexAdder = TypedIndexAdderImpl.create(params);
		TypedIndexUpsert<T> indexUpsert = TypedIndexUpsertImpl.create(params);
		TypedIndexRemover<I> indexRemover = TypedIndexRemoverImpl.create(params);
		IndexMergeService<T> mergeOperation = new IndexMergeServiceImpl<>(
			retrieveService, typedIndexAdder, indexUpsert);
		TypedIndexReset<T> indexReset = TypedIndexResetImpl.create(params);
		TypedIndexShallowUpdater<I, T> shallowUpdateService = createShallowUpdateService(params);
		return new WriteIndexOperationsFactory<>(indexWriter, typedIndexAdder, indexUpsert,
			indexRemover, mergeOperation, indexReset, shallowUpdateService, backupService);
	}

	public WriteIndexOperations<T, I> create() {
		return new WriteIndexOperationsImpl<>(indexWriter,
			indexAdder, indexUpsert, indexRemover, indexReset,
			shallowUpdateService, mergeService, backupService);
	}

	private static <T> IndexBackupService createBackupService(IndexOperationsParams<T> params) {
		return new IndexBackupServiceImpl(params.indexWriter());
	}

	private static <I, T> IndexReadService<I, T>
	createRetrieveService(IndexOperationsParams<T> params) {
		return IndexReadServiceImpl.create(params.typedIndexReaderParams());
	}

	private static <I, T> TypedIndexShallowUpdater<I, T>
	createShallowUpdateService(IndexOperationsParams<T> params) {
		return TypedIndexShallowUpdaterImpl.create(params);
	}
}
