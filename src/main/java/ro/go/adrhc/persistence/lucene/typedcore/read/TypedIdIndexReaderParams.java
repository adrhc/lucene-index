package ro.go.adrhc.persistence.lucene.typedcore.read;

import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;

public interface TypedIdIndexReaderParams {
	TypedField<?> getIdField();

	IndexReaderPool getIndexReaderPool();
}
