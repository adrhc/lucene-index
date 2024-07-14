package ro.go.adrhc.persistence.lucene.index.operations;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.operations.add.IndexAddServiceImpl;
import ro.go.adrhc.persistence.lucene.index.operations.count.IndexCountService;
import ro.go.adrhc.persistence.lucene.index.operations.merge.IndexMergeService;
import ro.go.adrhc.persistence.lucene.index.operations.merge.IndexMergeServiceImpl;
import ro.go.adrhc.persistence.lucene.index.operations.params.IndexServicesParamsFactory;
import ro.go.adrhc.persistence.lucene.index.operations.remove.IndexRemoveServiceImpl;
import ro.go.adrhc.persistence.lucene.index.operations.reset.IndexResetServiceImpl;
import ro.go.adrhc.persistence.lucene.index.operations.restore.IndexShallowUpdateServiceImpl;
import ro.go.adrhc.persistence.lucene.index.operations.retrieve.IndexRetrieveServiceImpl;
import ro.go.adrhc.persistence.lucene.index.operations.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.index.operations.update.IndexUpsertServiceImpl;
import ro.go.adrhc.persistence.lucene.typedcore.Indexable;

@RequiredArgsConstructor
public class IndexOperationsFactory<T extends Indexable<ID, T>, ID> {
	private final IndexCountService countService;
	private final IndexRetrieveServiceImpl<ID, T> retrieveService;
	private final IndexSearchService<T> searchService;
	private final IndexAddServiceImpl<T> addService;
	private final IndexUpsertServiceImpl<T> upsertService;
	private final IndexRemoveServiceImpl<ID> removeService;
	private final IndexMergeService<T> mergeService;
	private final IndexResetServiceImpl<T> resetService;
	private final IndexShallowUpdateServiceImpl<ID, T> shallowUpdateService;

	public static <T extends Indexable<ID, T>, ID>
	IndexOperationsFactory<T, ID> of(IndexServicesParamsFactory<T> params) {
		IndexServicesFactory<ID, T> srvFactory = new IndexServicesFactory<>(params);
		IndexCountService countService = srvFactory.createCountService();
		IndexRetrieveServiceImpl<ID, T> retrieveService = srvFactory.createIdSearchService();
		IndexSearchService<T> searchService = srvFactory.createSearchService();
		IndexAddServiceImpl<T> addService = srvFactory.createAddService();
		IndexUpsertServiceImpl<T> upsertService = srvFactory.createUpsertService();
		IndexRemoveServiceImpl<ID> removeService = srvFactory.createRemoveService();
		IndexMergeService<T> mergeService = new IndexMergeServiceImpl<>(
				retrieveService, addService, upsertService);
		IndexResetServiceImpl<T> resetService = srvFactory.createResetService();
		IndexShallowUpdateServiceImpl<ID, T> shallowUpdateService =
				srvFactory.createShallowUpdateService();
		return new IndexOperationsFactory<>(countService, retrieveService,
				searchService, addService, upsertService, removeService,
				mergeService, resetService, shallowUpdateService);
	}

	public WriteIndexOperations<T, ID> createWriteIndexOperations() {
		return new WriteIndexOperationsImpl<>(
				addService, upsertService, removeService, resetService,
				shallowUpdateService, mergeService);
	}

	public ReadIndexOperations<T, ID> createReadIndexOperations() {
		return new ReadIndexOperationsImpl<>(countService, retrieveService, searchService);
	}
}
