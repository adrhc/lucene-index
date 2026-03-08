package ro.go.adrhc.persistence.lucene.operations.restore;

import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexWriterParams;

public interface IndexShallowUpdateServiceParams<T>
	extends TypedIndexWriterParams<T>, TypedIndexRemoverParams {
	HitsLimitedIndexReaderParams<T> allHitsTypedIndexReaderParams();
}
