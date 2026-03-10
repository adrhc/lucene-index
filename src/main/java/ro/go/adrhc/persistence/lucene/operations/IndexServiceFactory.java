package ro.go.adrhc.persistence.lucene.operations;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexCounter;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexCounterImpl;
import ro.go.adrhc.persistence.lucene.core.typed.Identifiable;
import ro.go.adrhc.persistence.lucene.core.typed.read.IndexReadService;
import ro.go.adrhc.persistence.lucene.core.typed.read.IndexReadServiceImpl;
import ro.go.adrhc.persistence.lucene.core.typed.write.backup.IndexBackupService;
import ro.go.adrhc.persistence.lucene.core.typed.write.backup.IndexBackupServiceImpl;
import ro.go.adrhc.persistence.lucene.core.typed.write.shallow.TypedIndexShallowUpdater;
import ro.go.adrhc.persistence.lucene.core.typed.write.shallow.TypedIndexShallowUpdaterImpl;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServiceParamsFactory;
import ro.go.adrhc.persistence.lucene.core.typed.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.core.typed.search.IndexSearchServiceImpl;

@RequiredArgsConstructor
public class IndexServiceFactory<I, T extends Identifiable<I>> {
	private final IndexServiceParamsFactory<T> paramsFactory;

	public IndexBackupService createBackupService() {
		return new IndexBackupServiceImpl(paramsFactory.indexWriter());
	}

	public IndexSearchService<T> createSearchService() {
		return IndexSearchServiceImpl.create(paramsFactory.indexSearchServiceParams());
	}

	public IndexReadService<I, T> createRetrieveService() {
		return IndexReadServiceImpl.create(paramsFactory.typedIndexReaderParams());
	}

	public DocIndexCounter createDocIndexCounter() {
		return DocIndexCounterImpl.create(paramsFactory.docIndexReaderParams());
	}

	public TypedIndexShallowUpdater<I, T> createShallowUpdateService() {
		return TypedIndexShallowUpdaterImpl.create(paramsFactory);
	}
}
