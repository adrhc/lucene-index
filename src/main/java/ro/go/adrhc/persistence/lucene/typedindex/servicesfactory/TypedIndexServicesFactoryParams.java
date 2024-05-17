package ro.go.adrhc.persistence.lucene.typedindex.servicesfactory;

import ro.go.adrhc.persistence.lucene.typedcore.write.AbstractTypedIndexParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexUpdaterParams;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedIndexRetrieveServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchServiceParams;

public interface TypedIndexServicesFactoryParams<T> extends TypedIndexSearchServiceParams<T>,
		TypedIndexRestoreServiceParams<T>, TypedIndexRetrieveServiceParams<T>,
		TypedIndexUpdaterParams<T>, AbstractTypedIndexParams<T>, TypedIndexRemoverParams {
}
