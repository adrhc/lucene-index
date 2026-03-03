package ro.go.adrhc.persistence.lucene.core.typed.read;

import ro.go.adrhc.persistence.lucene.core.bare.read.HitsLimitedDocIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

public interface HitsLimitedIndexReaderParams<T>
	extends TypedIndexReaderParams<T>, HitsLimitedDocIndexReaderParams {
	LuceneFieldSpec<T> idField();
}
