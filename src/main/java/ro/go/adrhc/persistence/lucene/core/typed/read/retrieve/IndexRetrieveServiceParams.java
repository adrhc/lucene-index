package ro.go.adrhc.persistence.lucene.core.typed.read.retrieve;

import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderParams;

public interface IndexRetrieveServiceParams<T> extends TypedIndexReaderParams<T> {
	LuceneFieldSpec<T> idField();

	HitsLimitedIndexReaderParams<T> allHitsIndexReaderParams();
}
