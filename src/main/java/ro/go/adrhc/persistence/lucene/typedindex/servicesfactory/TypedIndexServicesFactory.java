package ro.go.adrhc.persistence.lucene.typedindex.servicesfactory;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.DocsCountService;
import ro.go.adrhc.persistence.lucene.typedcore.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedIndexAdderService;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.reset.TypedIndexResetService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedIndexRetrieveService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedIndexUpsertService;

@RequiredArgsConstructor
public class TypedIndexServicesFactory<ID, T extends Identifiable<ID>> {
	private final TypedIndexParams<T> params;

	public TypedIndexSearchService<T> createSearchService() {
		return TypedIndexSearchService.create(params.toTypedIndexSearchServiceParams());
	}

	public TypedIndexRetrieveService<ID, T> createIdSearchService() {
		return TypedIndexRetrieveService.create(params.toTypedIndexRetrieveServiceParams());
	}

	public DocsCountService createCountService() {
		return DocsCountService.create(params.getIndexReaderPool());
	}

	public TypedIndexRestoreService<ID, T> createRestoreService() {
		return TypedIndexRestoreService.create(params.toTypedIndexRestoreServiceParams());
	}

	public TypedIndexResetService<T> createResetService() {
		return TypedIndexResetService.create(params.toTypedIndexResetServiceParams());
	}

	public TypedIndexAdderService<T> createAdderService() {
		return TypedIndexAdderService.create(params.toTypedIndexAdderServiceParams());
	}

	public TypedIndexUpsertService<T> createUpdateService() {
		return TypedIndexUpsertService.create(params.toTypedIndexUpsertServiceParams());
	}

	public TypedIndexRemoveService<ID> createRemoveService() {
		return TypedIndexRemoveService.create(params.toTypedIndexRemoveServiceParams());
	}
}
