package ro.go.adrhc.persistence.lucene.operations.search;

import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderParams;

public interface IndexSearchServiceParams<T> extends TypedIndexReaderParams<T> {
	LuceneFieldSpec<T> idField();

	SearchResultFilter<T> searchResultFilter();

	TypedIndexReaderParams<T> typedIndexReaderParams();
}
