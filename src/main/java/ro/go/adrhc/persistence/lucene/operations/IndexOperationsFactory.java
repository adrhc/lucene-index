package ro.go.adrhc.persistence.lucene.operations;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.bare.write.DocIndexWriter;
import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.core.typed.write.*;
import ro.go.adrhc.persistence.lucene.operations.backup.IndexBackupService;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexCounter;
import ro.go.adrhc.persistence.lucene.operations.merge.IndexMergeService;
import ro.go.adrhc.persistence.lucene.operations.merge.IndexMergeServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServicesParamsFactory;
import ro.go.adrhc.persistence.lucene.operations.restore.IndexShallowUpdateServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.retrieve.IndexRetrieveServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.search.IndexSearchService;

@RequiredArgsConstructor
public class IndexOperationsFactory<T extends Indexable<I, T>, I> {
	private final LuceneFieldSpec<T> idField;
	private final HitsLimitedIndexReaderTemplate<I, T> unlimitedIdxReaderTemplate;
	private final DocIndexCounter indexCounter;
	private final IndexRetrieveServiceImpl<I, T> retrieveService;
	private final IndexSearchService<T> searchService;
	private final DocIndexWriter indexWriter;
	private final TypedIndexAdder<T> indexAdder;
	private final TypedIndexUpsert<T> indexUpsert;
	private final TypedIndexRemover<I> indexRemover;
	private final IndexMergeService<T> mergeService;
	private final TypedIndexReset<T> indexReset;
	private final IndexShallowUpdateServiceImpl<I, T> shallowUpdateService;
	private final IndexBackupService backupService;

	public static <T extends Indexable<I, T>, I>
	IndexOperationsFactory<T, I> of(IndexServicesParamsFactory<T> params) {
		IndexServiceFactory<I, T> srvFactory = new IndexServiceFactory<>(params);
		DocIndexCounter indexCounter = srvFactory.createDocIndexCounter();
		IndexRetrieveServiceImpl<I, T> retrieveService = srvFactory.createRetrieveService();
		IndexBackupService backupService = srvFactory.createBackupService();
		IndexSearchService<T> searchService = srvFactory.createSearchService();
		DocIndexWriter indexWriter = new DocIndexWriter(params.indexWriter());
		TypedIndexAdder<T> typedIndexAdder = TypedIndexAdderImpl.create(params);
		TypedIndexUpsert<T> indexUpsert = TypedIndexUpsertImpl.create(params);
		TypedIndexRemover<I> indexRemover = TypedIndexRemoverImpl.create(params);
		IndexMergeService<T> mergeService = new IndexMergeServiceImpl<>(
			retrieveService, typedIndexAdder, indexUpsert);
		TypedIndexReset<T> indexReset = TypedIndexResetImpl.create(params);
		IndexShallowUpdateServiceImpl<I, T> shallowUpdateService =
			srvFactory.createShallowUpdateService();
		HitsLimitedIndexReaderTemplate<I, T> unlimitedIdxReaderTemplate =
			HitsLimitedIndexReaderTemplate.create(params.allHitsTypedIndexReaderParams());
		return new IndexOperationsFactory<>(params.idField(), unlimitedIdxReaderTemplate,
			indexCounter, retrieveService, searchService, indexWriter, typedIndexAdder, indexUpsert,
			indexRemover, mergeService, indexReset, shallowUpdateService, backupService);
	}

	public WriteIndexOperations<T, I> createWriteIndexOperations() {
		return new WriteIndexOperationsImpl<>(indexWriter,
			indexAdder, indexUpsert, indexRemover, indexReset,
			shallowUpdateService, mergeService, backupService);
	}

	public ReadDocIndexOperations<T, I> createReadIndexOperations() {
		return new ReadDocIndexOperationsImpl<>(idField, unlimitedIdxReaderTemplate,
			indexCounter, retrieveService, searchService);
	}
}
