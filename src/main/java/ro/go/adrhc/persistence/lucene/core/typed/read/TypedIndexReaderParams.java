package ro.go.adrhc.persistence.lucene.core.typed.read;

import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.field.RawFieldValueSerdes;

/**
 * Used by OneHitIndexReader.
 */
public interface TypedIndexReaderParams<T> extends DocIndexReaderParams {
	RawFieldValueSerdes<T> rawFieldValueSerdes();
}
