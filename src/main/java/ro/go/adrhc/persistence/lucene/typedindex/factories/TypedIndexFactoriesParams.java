package ro.go.adrhc.persistence.lucene.typedindex.factories;

import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexUpdaterParams;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedIndexRetrieveServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchServiceParams;

public interface TypedIndexFactoriesParams<T> extends TypedIndexSearchServiceParams<T>,
        TypedIndexRestoreServiceParams<T>,
        TypedIndexRetrieveServiceParams<T>, TypedIndexUpdaterParams<T>, ro.go.adrhc.persistence.lucene.typedcore.write.AbstractTypedIndexParams<T>, ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemoverParams {
}
