package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.NoArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.IndexCountServiceImpl;
import ro.go.adrhc.persistence.lucene.typedcore.Indexable;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedAddService;
import ro.go.adrhc.persistence.lucene.typedindex.merge.IndexMergeService;
import ro.go.adrhc.persistence.lucene.typedindex.merge.IndexMergeServiceImpl;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.reset.TypedResetService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedShallowUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedRetrieveService;
import ro.go.adrhc.persistence.lucene.typedindex.search.IndexSearchServiceImpl;
import ro.go.adrhc.persistence.lucene.typedindex.srvparams.IndexServicesParamsFactory;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedUpsertService;

@NoArgsConstructor
public class IndexOperationsImplBuilder<ID, T extends Indexable<ID, T>> {
	private IndexServicesParamsFactory<T> params;
	private TypedShallowUpdateService<ID, T> shallowUpdateService;
	private TypedResetService<T> resetService;

	public static <ID, T extends Indexable<ID, T>> IndexOperations<ID, T>
	createIndexOperations(IndexServicesParamsFactory<T> params) {
		return IndexOperationsImplBuilder.of(params).build();
	}

	public static <ID, T extends Indexable<ID, T>>
	IndexOperationsImplBuilder<ID, T> of(IndexServicesParamsFactory<T> params) {
		IndexOperationsImplBuilder<ID, T> builder = new IndexOperationsImplBuilder<>();
		return builder.params(params);
	}

	public IndexOperationsImplBuilder<ID, T> params(IndexServicesParamsFactory<T> params) {
		this.params = params;
		return this;
	}

	public IndexOperationsImplBuilder<ID, T>
	shallowUpdateService(TypedShallowUpdateService<ID, T> shallowUpdateService) {
		this.shallowUpdateService = shallowUpdateService;
		return this;
	}

	public IndexOperationsImplBuilder<ID, T>
	resetService(TypedResetService<T> resetService) {
		this.resetService = resetService;
		return this;
	}

	public IndexOperations<ID, T> build() {
		IndexServicesFactory<ID, T> srvFactory = new IndexServicesFactory<>(params);
		IndexSearchServiceImpl<T> searchService = srvFactory.createSearchService();
		TypedRetrieveService<ID, T> retrieveService = srvFactory.createIdSearchService();
		IndexCountServiceImpl countService = srvFactory.createCountService();
		TypedAddService<T> addService = srvFactory.createAddService();
		TypedUpsertService<T> upsertService = srvFactory.createUpsertService();
		TypedRemoveService<ID> removeService = srvFactory.createRemoveService();
		IndexMergeService<T> mergeService = new IndexMergeServiceImpl<>(
				retrieveService, addService, upsertService);
		TypedResetService<T> resetService = this.resetService == null ?
				srvFactory.createResetService() : this.resetService;
		TypedShallowUpdateService<ID, T> shallowUpdateService = this.shallowUpdateService == null ?
				srvFactory.createShallowUpdateService() : this.shallowUpdateService;
		return new IndexOperationsImpl<>(searchService, retrieveService, countService,
				addService, upsertService, removeService, resetService,
				shallowUpdateService, mergeService);
	}
}
