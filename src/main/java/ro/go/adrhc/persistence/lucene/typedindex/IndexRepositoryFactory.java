package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.experimental.UtilityClass;
import ro.go.adrhc.persistence.lucene.index.DocsCountService;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedIndexAdderService;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.reset.TypedIndexResetService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedIndexRetrieveService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexParams;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexParamsImpl;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexServicesFactory;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedIndexUpsertService;

@UtilityClass
public class IndexRepositoryFactory {
	public static <ID, T extends Identifiable<ID>>
	IndexRepository<ID, T> create(TypedIndexParams<ID, T> context) {
		IndexOperations<ID, T> indexOperations = createIndexOperations(context);
		return new IndexRepositoryImpl<>(indexOperations, context);
	}

	public static <ID, T extends Identifiable<ID>>
	ReadOnlyIndexOperations<ID, T> createReadOnlyIndexOperations(
			TypedIndexParamsImpl<ID, T> context) {
		return create(context);
	}

	public static <ID, T extends Identifiable<ID>> IndexOperations<ID, T>
	createIndexOperations(TypedIndexParams<ID, T> params) {
		TypedIndexServicesFactory<ID, T> factories = new TypedIndexServicesFactory<>(params);
		TypedIndexSearchService<T> searchService = factories.createSearchService();
		TypedIndexRetrieveService<ID, T> retrieveService = factories.createIdSearchService();
		DocsCountService countService = factories.createCountService();
		TypedIndexAdderService<T> adderService = factories.createAdderService();
		TypedIndexUpsertService<T> updateService = factories.createUpdateService();
		TypedIndexRemoveService<ID> removeService = factories.createRemoveService();
		TypedIndexResetService<T> resetService = factories.createResetService();
		TypedIndexRestoreService<ID, T> restoreService = factories.createRestoreService();
		return new IndexOperationsImpl<>(searchService, retrieveService, countService,
				adderService, updateService, removeService, resetService, restoreService);
	}
}
