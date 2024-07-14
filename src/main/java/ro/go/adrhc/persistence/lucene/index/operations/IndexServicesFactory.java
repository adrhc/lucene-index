package ro.go.adrhc.persistence.lucene.index.operations;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.operations.add.IndexAddServiceImpl;
import ro.go.adrhc.persistence.lucene.index.operations.count.IndexCountServiceImpl;
import ro.go.adrhc.persistence.lucene.index.operations.params.IndexServicesParamsFactory;
import ro.go.adrhc.persistence.lucene.index.operations.remove.IndexRemoveServiceImpl;
import ro.go.adrhc.persistence.lucene.index.operations.reset.IndexResetServiceImpl;
import ro.go.adrhc.persistence.lucene.index.operations.restore.IndexShallowUpdateServiceImpl;
import ro.go.adrhc.persistence.lucene.index.operations.retrieve.IndexRetrieveServiceImpl;
import ro.go.adrhc.persistence.lucene.index.operations.search.IndexSearchServiceImpl;
import ro.go.adrhc.persistence.lucene.index.operations.update.IndexUpsertServiceImpl;
import ro.go.adrhc.persistence.lucene.typedcore.Identifiable;

@RequiredArgsConstructor
public class IndexServicesFactory<ID, T extends Identifiable<ID>> {
	private final IndexServicesParamsFactory<T> paramsFactory;

	public IndexSearchServiceImpl<T> createSearchService() {
		return IndexSearchServiceImpl.create(paramsFactory.indexSearchServiceParams());
	}

	public IndexRetrieveServiceImpl<ID, T> createIdSearchService() {
		return IndexRetrieveServiceImpl.create(paramsFactory.typedRetrieveServiceParams());
	}

	public IndexCountServiceImpl createCountService() {
		return IndexCountServiceImpl.create(paramsFactory.getIndexReaderPool());
	}

	public IndexShallowUpdateServiceImpl<ID, T> createShallowUpdateService() {
		return IndexShallowUpdateServiceImpl.create(
				paramsFactory.typedShallowUpdateServiceParams());
	}

	public IndexResetServiceImpl<T> createResetService() {
		return IndexResetServiceImpl.create(paramsFactory.typedIndexWriterParams());
	}

	public IndexAddServiceImpl<T> createAddService() {
		return IndexAddServiceImpl.create(paramsFactory.typedAddServiceParams());
	}

	public IndexUpsertServiceImpl<T> createUpsertService() {
		return IndexUpsertServiceImpl.create(paramsFactory.typedIndexUpsertParams());
	}

	public IndexRemoveServiceImpl<ID> createRemoveService() {
		return IndexRemoveServiceImpl.create(paramsFactory.typedIndexRemoverParams());
	}
}
