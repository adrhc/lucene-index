package ro.go.adrhc.persistence.lucene.operations.search;

import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

public record IndexSearchServiceParamsImpl<T>(Class<T> type, LuceneFieldSpec<T> idField,
	IndexReaderPool indexReaderPool, SearchResultFilter<T> searchResultFilter, int numHits)
	implements IndexSearchServiceParams<T> {
}
