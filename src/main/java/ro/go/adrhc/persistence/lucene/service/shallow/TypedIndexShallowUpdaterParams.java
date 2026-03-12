package ro.go.adrhc.persistence.lucene.service.shallow;

import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexWriterParams;

public interface TypedIndexShallowUpdaterParams<T>
	extends TypedIndexWriterParams<T>, TypedIndexRemoverParams {
	TypedIndexReaderParams<T> typedIndexReaderParams();
}
