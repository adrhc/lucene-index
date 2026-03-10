package ro.go.adrhc.persistence.lucene.operations;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexCounter;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexCounterImpl;
import ro.go.adrhc.persistence.lucene.core.typed.Identifiable;
import ro.go.adrhc.persistence.lucene.core.typed.read.retrieve.IndexRetrieveService;
import ro.go.adrhc.persistence.lucene.core.typed.read.retrieve.IndexRetrieveServiceImpl;
import ro.go.adrhc.persistence.lucene.core.typed.write.backup.IndexBackupService;
import ro.go.adrhc.persistence.lucene.core.typed.write.backup.IndexBackupServiceImpl;
import ro.go.adrhc.persistence.lucene.core.typed.write.shallow.TypedIndexShallowUpdater;
import ro.go.adrhc.persistence.lucene.core.typed.write.shallow.TypedIndexShallowUpdaterImpl;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServicesParamsFactory;
import ro.go.adrhc.persistence.lucene.operations.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.operations.search.IndexSearchServiceImpl;

@RequiredArgsConstructor
public class IndexServiceFactory<I, T extends Identifiable<I>> {
	private final IndexServicesParamsFactory<T> paramsFactory;

	public IndexBackupService createBackupService() {
		return new IndexBackupServiceImpl(paramsFactory.indexWriter());
	}

	public IndexSearchService<T> createSearchService() {
		return IndexSearchServiceImpl.create(paramsFactory.indexSearchServiceParams());
	}

	public IndexRetrieveService<I, T> createRetrieveService() {
		return IndexRetrieveServiceImpl.create(paramsFactory.indexRetrieveServiceParams());
	}

	public DocIndexCounter createDocIndexCounter() {
		return DocIndexCounterImpl.create(paramsFactory.docIndexReaderParams());
	}

	public TypedIndexShallowUpdater<I, T> createShallowUpdateService() {
		return TypedIndexShallowUpdaterImpl.create(paramsFactory);
	}
}
