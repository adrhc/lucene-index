package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.NoArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.DocsCountService;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedIndexAdderService;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.reset.TypedIndexResetService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedIndexRetrieveService;
import ro.go.adrhc.persistence.lucene.typedindex.search.DefaultIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexParams;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexServicesFactory;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedIndexUpsertService;

@NoArgsConstructor
public class DefaultIndexOperationsBuilder<ID, T extends Indexable<ID, T>> {
	private TypedIndexParams<T> params;
	private TypedIndexRestoreService<ID, T> restoreService;
	private TypedIndexResetService<T> resetService;

	public static <ID, T extends Indexable<ID, T>>
	DefaultIndexOperationsBuilder<ID, T> of(TypedIndexParams<T> params) {
		DefaultIndexOperationsBuilder<ID, T> builder = new DefaultIndexOperationsBuilder<>();
		return builder.params(params);
	}

	public DefaultIndexOperationsBuilder<ID, T> params(TypedIndexParams<T> params) {
		this.params = params;
		return this;
	}

	public DefaultIndexOperationsBuilder<ID, T>
	restoreService(TypedIndexRestoreService<ID, T> restoreService) {
		this.restoreService = restoreService;
		return this;
	}

	public DefaultIndexOperationsBuilder<ID, T>
	resetService(TypedIndexResetService<T> resetService) {
		this.resetService = resetService;
		return this;
	}

	public DefaultIndexOperations<ID, T> build() {
		TypedIndexServicesFactory<ID, T> factories = new TypedIndexServicesFactory<>(params);
		DefaultIndexSearchService<T> searchService = factories.createSearchService();
		TypedIndexRetrieveService<ID, T> retrieveService = factories.createIdSearchService();
		DocsCountService countService = factories.createCountService();
		TypedIndexAdderService<T> adderService = factories.createAdderService();
		TypedIndexUpsertService<T> updateService = factories.createUpdateService();
		TypedIndexRemoveService<ID> removeService = factories.createRemoveService();
		TypedIndexResetService<T> resetService = this.resetService == null ?
				factories.createResetService() : this.resetService;
		TypedIndexRestoreService<ID, T> restoreService = this.restoreService == null ?
				factories.createRestoreService() : this.restoreService;
		return new DefaultIndexOperations<>(searchService, retrieveService, countService,
				adderService, updateService, removeService, resetService, restoreService);
	}
}
