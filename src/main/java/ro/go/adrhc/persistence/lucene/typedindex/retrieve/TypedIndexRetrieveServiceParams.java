package ro.go.adrhc.persistence.lucene.typedindex.retrieve;

import ro.go.adrhc.persistence.lucene.typedcore.read.OneHitIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParams;

public interface TypedIndexRetrieveServiceParams<T> extends
        TypedIndexReaderParams<T>, OneHitIndexReaderParams<T> {
}
