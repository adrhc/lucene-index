package ro.go.adrhc.persistence.lucene.typedindex.retrieve;

import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIdIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParams;

public interface TypedIndexRetrieveServiceParams<T> extends
		TypedIndexReaderParams<T>, TypedIdIndexReaderParams {
}
