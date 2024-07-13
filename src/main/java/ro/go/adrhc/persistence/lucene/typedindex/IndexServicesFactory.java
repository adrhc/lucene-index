package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.IndexCountServiceImpl;
import ro.go.adrhc.persistence.lucene.typedcore.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedAddService;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.reset.TypedResetService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedShallowUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedRetrieveService;
import ro.go.adrhc.persistence.lucene.typedindex.search.IndexSearchServiceImpl;
import ro.go.adrhc.persistence.lucene.typedindex.srvparams.IndexServicesParamsFactory;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedUpsertService;

@RequiredArgsConstructor
public class IndexServicesFactory<ID, T extends Identifiable<ID>> {
	private final IndexServicesParamsFactory<T> paramsFactory;

	public IndexSearchServiceImpl<T> createSearchService() {
		return IndexSearchServiceImpl.create(paramsFactory.indexSearchServiceParams());
	}

	public TypedRetrieveService<ID, T> createIdSearchService() {
		return TypedRetrieveService.create(paramsFactory.typedRetrieveServiceParams());
	}

	public IndexCountServiceImpl createCountService() {
		return IndexCountServiceImpl.create(paramsFactory.getIndexReaderPool());
	}

	public TypedShallowUpdateService<ID, T> createShallowUpdateService() {
		return TypedShallowUpdateService.create(paramsFactory.typedShallowUpdateServiceParams());
	}

	public TypedResetService<T> createResetService() {
		return TypedResetService.create(paramsFactory.typedIndexWriterParams());
	}

	public TypedAddService<T> createAddService() {
		return TypedAddService.create(paramsFactory.typedAddServiceParams());
	}

	public TypedUpsertService<T> createUpsertService() {
		return TypedUpsertService.create(paramsFactory.typedIndexUpsertParams());
	}

	public TypedRemoveService<ID> createRemoveService() {
		return TypedRemoveService.create(paramsFactory.typedIndexRemoverParams());
	}
}
