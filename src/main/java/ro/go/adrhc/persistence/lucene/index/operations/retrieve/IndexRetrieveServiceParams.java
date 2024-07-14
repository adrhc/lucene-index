package ro.go.adrhc.persistence.lucene.index.operations.retrieve;

import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.typedcore.read.OneHitIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParams;

public interface IndexRetrieveServiceParams<T> extends OneHitIndexReaderParams<T> {
	LuceneFieldSpec<T> getIdField();

	TypedIndexReaderParams<T> allHitsTypedIndexReaderParams();
}
