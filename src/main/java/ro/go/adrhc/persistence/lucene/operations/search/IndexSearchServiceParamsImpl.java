package ro.go.adrhc.persistence.lucene.operations.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

@RequiredArgsConstructor
@Getter
public class IndexSearchServiceParamsImpl<T> implements IndexSearchServiceParams<T> {
	private final Class<T> type;
	private final LuceneFieldSpec<T> idField;
	private final IndexReaderPool indexReaderPool;
	private final SearchResultFilter<T> searchResultFilter;
	private final int numHits;
}
