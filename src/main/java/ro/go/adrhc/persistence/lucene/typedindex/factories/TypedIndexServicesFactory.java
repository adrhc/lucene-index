package ro.go.adrhc.persistence.lucene.typedindex.factories;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.DocsCountService;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedIndexAdderService;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.reset.TypedIndexResetService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedIndexRetrieveService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedIndexUpsertService;

@RequiredArgsConstructor
public class TypedIndexServicesFactory<ID, T extends Identifiable<ID>> {
    private final TypedIndexServicesFactoryParams<T> params;

    public TypedIndexSearchService<T> createSearchService() {
        return TypedIndexSearchService.create(params);
    }

    public TypedIndexRetrieveService<ID, T> createIdSearchService() {
        return TypedIndexRetrieveService.create(params);
    }

    public DocsCountService createCountService() {
        return DocsCountService.create(params.getIndexReaderPool());
    }

    public TypedIndexRestoreService<ID, T> createRestoreService() {
        return TypedIndexRestoreService.create(params);
    }

    public TypedIndexResetService<T> createResetService() {
        return TypedIndexResetService.create(params);
    }

    public TypedIndexAdderService<T> createAdderService() {
        return TypedIndexAdderService.create(params);
    }

    public TypedIndexUpsertService<T> createUpdateService() {
        return TypedIndexUpsertService.create(params);
    }

    public TypedIndexRemoveService<ID> createRemoveService() {
        return TypedIndexRemoveService.create(params);
    }
}
