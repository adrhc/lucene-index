package ro.go.adrhc.persistence.lucene.typedindex.servicesfactory;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.IndexCountServiceImpl;
import ro.go.adrhc.persistence.lucene.typedcore.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedAddService;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.reset.TypedResetService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedShallowUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedRetrieveService;
import ro.go.adrhc.persistence.lucene.typedindex.search.IndexSearchServiceImpl;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedUpsertService;

@RequiredArgsConstructor
public class TypedIndexServicesFactory<ID, T extends Identifiable<ID>> {
	private final TypedIndexParams<T> params;

	public IndexSearchServiceImpl<T> createSearchService() {
		return IndexSearchServiceImpl.create(params.toTypedIndexSearchServiceParams());
	}

	public TypedRetrieveService<ID, T> createIdSearchService() {
		return TypedRetrieveService.create(params.toTypedRetrieveServiceParams());
	}

	public IndexCountServiceImpl createCountService() {
		return IndexCountServiceImpl.create(params.getIndexReaderPool());
	}

	public TypedShallowUpdateService<ID, T> createShallowUpdateService() {
		return TypedShallowUpdateService.create(params.toTypedShallowUpdateServiceParams());
	}

	public TypedResetService<T> createResetService() {
		return TypedResetService.create(params.toTypedResetServiceParams());
	}

	public TypedAddService<T> createAddService() {
		return TypedAddService.create(params.toTypedAddServiceParams());
	}

	public TypedUpsertService<T> createUpsertService() {
		return TypedUpsertService.create(params.toTypedIndexUpsertParams());
	}

	public TypedRemoveService<ID> createRemoveService() {
		return TypedRemoveService.create(params.toTypedRemoveServiceParams());
	}
}
