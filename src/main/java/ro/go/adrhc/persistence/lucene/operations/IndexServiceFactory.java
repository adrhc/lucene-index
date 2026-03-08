package ro.go.adrhc.persistence.lucene.operations;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.typed.Identifiable;
import ro.go.adrhc.persistence.lucene.operations.backup.IndexBackupService;
import ro.go.adrhc.persistence.lucene.operations.backup.IndexBackupServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.count.IndexCountServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServicesParamsFactory;
import ro.go.adrhc.persistence.lucene.operations.restore.IndexShallowUpdateServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.retrieve.IndexRetrieveServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.search.IndexSearchServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.update.IndexUpsertServiceImpl;

@RequiredArgsConstructor
public class IndexServiceFactory<I, T extends Identifiable<I>> {
	private final IndexServicesParamsFactory<T> paramsFactory;

	public IndexBackupService createBackupService() {
		return new IndexBackupServiceImpl(paramsFactory.indexWriter());
	}

	public IndexSearchServiceImpl<T> createSearchService() {
		return IndexSearchServiceImpl.create(paramsFactory.indexSearchServiceParams());
	}

	public IndexRetrieveServiceImpl<I, T> createRetrieveService() {
		return IndexRetrieveServiceImpl.create(paramsFactory.typedRetrieveServiceParams());
	}

	public IndexCountServiceImpl createCountService() {
		return IndexCountServiceImpl.create(paramsFactory.indexCountServiceParams());
	}

	public IndexShallowUpdateServiceImpl<I, T> createShallowUpdateService() {
		return IndexShallowUpdateServiceImpl.create(paramsFactory);
	}

	public IndexUpsertServiceImpl<T> createUpsertService() {
		return IndexUpsertServiceImpl.create(paramsFactory);
	}
}
