package ro.go.adrhc.persistence.lucene.operations.retrieve;

import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.read.OneHitIndexReaderParams;

public interface IndexRetrieveServiceParams<T> extends OneHitIndexReaderParams<T> {
	LuceneFieldSpec<T> getIdField();

	HitsLimitedIndexReaderParams<T> allHitsTypedIndexReaderParams();
}
