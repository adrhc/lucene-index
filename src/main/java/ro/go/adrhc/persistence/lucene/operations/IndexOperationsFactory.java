package ro.go.adrhc.persistence.lucene.operations;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexCounter;
import ro.go.adrhc.persistence.lucene.core.bare.write.DocIndexWriter;
import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.read.IndexReadService;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.core.typed.write.*;
import ro.go.adrhc.persistence.lucene.core.typed.write.backup.IndexBackupService;
import ro.go.adrhc.persistence.lucene.core.typed.write.shallow.TypedIndexShallowUpdater;
import ro.go.adrhc.persistence.lucene.operations.merge.IndexMergeService;
import ro.go.adrhc.persistence.lucene.operations.merge.IndexMergeServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServiceParamsFactory;
import ro.go.adrhc.persistence.lucene.operations.search.IndexSearchService;

@RequiredArgsConstructor
public class IndexOperationsFactory<T extends Indexable<I, T>, I> {
	private final LuceneFieldSpec<T> idField;
	private final TypedIndexReaderTemplate<I, T> unlimitedIdxReaderTemplate;
	private final DocIndexCounter indexCounter;
	private final IndexReadService<I, T> retrieveService;
	private final IndexSearchService<T> searchService;
	private final DocIndexWriter indexWriter;
	private final TypedIndexAdder<T> indexAdder;
	private final TypedIndexUpsert<T> indexUpsert;
	private final TypedIndexRemover<I> indexRemover;
	private final IndexMergeService<T> mergeService;
	private final TypedIndexReset<T> indexReset;
	private final TypedIndexShallowUpdater<I, T> shallowUpdateService;
	private final IndexBackupService backupService;

	public static <T extends Indexable<I, T>, I>
	IndexOperationsFactory<T, I> of(IndexServiceParamsFactory<T> params) {
		IndexServiceFactory<I, T> srvFactory = new IndexServiceFactory<>(params);
		DocIndexCounter indexCounter = srvFactory.createDocIndexCounter();
		IndexReadService<I, T> retrieveService = srvFactory.createRetrieveService();
		IndexBackupService backupService = srvFactory.createBackupService();
		IndexSearchService<T> searchService = srvFactory.createSearchService();
		DocIndexWriter indexWriter = new DocIndexWriter(params.indexWriter());
		TypedIndexAdder<T> typedIndexAdder = TypedIndexAdderImpl.create(params);
		TypedIndexUpsert<T> indexUpsert = TypedIndexUpsertImpl.create(params);
		TypedIndexRemover<I> indexRemover = TypedIndexRemoverImpl.create(params);
		IndexMergeService<T> mergeService = new IndexMergeServiceImpl<>(
			retrieveService, typedIndexAdder, indexUpsert);
		TypedIndexReset<T> indexReset = TypedIndexResetImpl.create(params);
		TypedIndexShallowUpdater<I, T> shallowUpdateService =
			srvFactory.createShallowUpdateService();
		TypedIndexReaderTemplate<I, T> typedIndexReaderTemplate =
			TypedIndexReaderTemplate.create(params.typedIndexReaderParams());
		return new IndexOperationsFactory<>(params.idField(), typedIndexReaderTemplate,
			indexCounter, retrieveService, searchService, indexWriter, typedIndexAdder, indexUpsert,
			indexRemover, mergeService, indexReset, shallowUpdateService, backupService);
	}

	public WriteIndexOperations<T, I> createWriteIndexOperations() {
		return new WriteIndexOperationsImpl<>(indexWriter,
			indexAdder, indexUpsert, indexRemover, indexReset,
			shallowUpdateService, mergeService, backupService);
	}

	public ReadIndexOperations<T, I> createReadIndexOperations() {
		return new ReadIndexOperationsImpl<>(idField, unlimitedIdxReaderTemplate,
			indexCounter, retrieveService, searchService);
	}
}
