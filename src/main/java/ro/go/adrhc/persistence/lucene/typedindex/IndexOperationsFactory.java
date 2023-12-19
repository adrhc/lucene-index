package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.experimental.UtilityClass;
import ro.go.adrhc.persistence.lucene.index.DocsCountService;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedIndexAdderService;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactories;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactoriesParams;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.reset.TypedIndexResetService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedIndexRetrieveService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedIndexUpsertService;

@UtilityClass
public class IndexOperationsFactory {
    public static <ID, T extends Identifiable<ID>> IndexOperations<ID, T>
    create(TypedIndexFactoriesParams<T> params) {
        TypedIndexFactories<ID, T> factories = new TypedIndexFactories<>(params);
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
