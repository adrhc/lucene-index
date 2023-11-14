package ro.go.adrhc.persistence.lucene.typedindex.restore;

import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIdIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexAdderParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemoverParams;

public interface TypedIndexRestoreServiceParams<T> extends
		TypedIdIndexReaderParams, TypedIndexAdderParams<T>, TypedIndexRemoverParams {
}
