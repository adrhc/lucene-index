package ro.go.adrhc.persistence.lucene.core.typed.read;

import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.field.RawFieldValueSerdes;

public interface TypedIndexReaderParams<T> extends DocIndexReaderParams {
	LuceneFieldSpec<T> idField();

	RawFieldValueSerdes<T> rawFieldValueSerdes();
}
