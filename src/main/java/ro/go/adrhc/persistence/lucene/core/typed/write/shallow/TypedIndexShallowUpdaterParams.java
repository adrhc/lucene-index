package ro.go.adrhc.persistence.lucene.core.typed.write.shallow;

import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexWriterParams;

public interface TypedIndexShallowUpdaterParams<T>
	extends TypedIndexWriterParams<T>, TypedIndexRemoverParams {
	HitsLimitedIndexReaderParams<T> allHitsIndexReaderParams();
}
