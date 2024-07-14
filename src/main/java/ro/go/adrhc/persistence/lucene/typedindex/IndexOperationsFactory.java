package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.IndexCountService;
import ro.go.adrhc.persistence.lucene.typedcore.Indexable;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedAddService;
import ro.go.adrhc.persistence.lucene.typedindex.merge.IndexMergeService;
import ro.go.adrhc.persistence.lucene.typedindex.merge.IndexMergeServiceImpl;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.reset.TypedResetService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedShallowUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedRetrieveService;
import ro.go.adrhc.persistence.lucene.typedindex.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.srvparams.IndexServicesParamsFactory;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedUpsertService;

@RequiredArgsConstructor
public class IndexOperationsFactory<T extends Indexable<ID, T>, ID> {
	private final IndexCountService countService;
	private final TypedRetrieveService<ID, T> retrieveService;
	private final IndexSearchService<T> searchService;
	private final TypedAddService<T> addService;
	private final TypedUpsertService<T> upsertService;
	private final TypedRemoveService<ID> removeService;
	private final IndexMergeService<T> mergeService;
	private final TypedResetService<T> resetService;
	private final TypedShallowUpdateService<ID, T> shallowUpdateService;

	public static <T extends Indexable<ID, T>, ID>
	IndexOperationsFactory<T, ID> of(IndexServicesParamsFactory<T> params) {
		IndexServicesFactory<ID, T> srvFactory = new IndexServicesFactory<>(params);
		IndexCountService countService = srvFactory.createCountService();
		TypedRetrieveService<ID, T> retrieveService = srvFactory.createIdSearchService();
		IndexSearchService<T> searchService = srvFactory.createSearchService();
		TypedAddService<T> addService = srvFactory.createAddService();
		TypedUpsertService<T> upsertService = srvFactory.createUpsertService();
		TypedRemoveService<ID> removeService = srvFactory.createRemoveService();
		IndexMergeService<T> mergeService = new IndexMergeServiceImpl<>(
				retrieveService, addService, upsertService);
		TypedResetService<T> resetService = srvFactory.createResetService();
		TypedShallowUpdateService<ID, T> shallowUpdateService =
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
