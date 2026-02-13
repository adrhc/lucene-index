package ro.go.adrhc.persistence.lucene.operations;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.typed.Identifiable;
import ro.go.adrhc.persistence.lucene.operations.add.IndexAddServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.backup.IndexBackupService;
import ro.go.adrhc.persistence.lucene.operations.backup.IndexBackupServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.count.IndexCountServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServicesParamsFactory;
import ro.go.adrhc.persistence.lucene.operations.remove.IndexRemoveServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.reset.IndexResetServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.restore.IndexShallowUpdateServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.retrieve.IndexRetrieveServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.search.IndexSearchServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.update.IndexUpsertServiceImpl;

@RequiredArgsConstructor
public class IndexServiceFactory<ID, T extends Identifiable<ID>> {
	private final IndexServicesParamsFactory<T> paramsFactory;

	public IndexBackupService createBackupService() {
		return new IndexBackupServiceImpl(paramsFactory.getIndexWriter());
	}

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
