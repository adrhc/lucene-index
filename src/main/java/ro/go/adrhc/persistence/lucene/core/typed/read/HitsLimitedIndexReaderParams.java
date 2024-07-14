package ro.go.adrhc.persistence.lucene.core.typed.read;

import ro.go.adrhc.persistence.lucene.core.bare.read.HitsLimitedDocsIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

public interface HitsLimitedIndexReaderParams<T> extends HitsLimitedDocsIndexReaderParams {
	Class<T> getType();

	LuceneFieldSpec<T> getIdField();
}
