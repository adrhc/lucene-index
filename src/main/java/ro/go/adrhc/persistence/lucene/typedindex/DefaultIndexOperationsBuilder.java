package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.NoArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.DefaultIndexCountService;
import ro.go.adrhc.persistence.lucene.typedcore.Indexable;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedAddService;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.reset.TypedResetService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedShallowUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedRetrieveService;
import ro.go.adrhc.persistence.lucene.typedindex.search.DefaultIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexParams;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexServicesFactory;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedUpsertService;

@NoArgsConstructor
public class DefaultIndexOperationsBuilder<ID, T extends Indexable<ID, T>> {
	private TypedIndexParams<T> params;
	private TypedShallowUpdateService<ID, T> shallowUpdateService;
	private TypedResetService<T> resetService;

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
	shallowUpdateService(TypedShallowUpdateService<ID, T> shallowUpdateService) {
		this.shallowUpdateService = shallowUpdateService;
		return this;
	}

	public DefaultIndexOperationsBuilder<ID, T>
	resetService(TypedResetService<T> resetService) {
		this.resetService = resetService;
		return this;
	}

	public DefaultIndexOperations<ID, T> build() {
		TypedIndexServicesFactory<ID, T> factories = new TypedIndexServicesFactory<>(params);
		DefaultIndexSearchService<T> searchService = factories.createSearchService();
		TypedRetrieveService<ID, T> retrieveService = factories.createIdSearchService();
		DefaultIndexCountService countService = factories.createCountService();
		TypedAddService<T> adderService = factories.createAddService();
		TypedUpsertService<T> updateService = factories.createUpsertService();
		TypedRemoveService<ID> removeService = factories.createRemoveService();
		TypedResetService<T> resetService = this.resetService == null ?
				factories.createResetService() : this.resetService;
		TypedShallowUpdateService<ID, T> shallowUpdateService = this.shallowUpdateService == null ?
				factories.createShallowUpdateService() : this.shallowUpdateService;
		return new DefaultIndexOperations<>(searchService, retrieveService, countService,
				adderService, updateService, removeService, resetService, shallowUpdateService);
	}
}
