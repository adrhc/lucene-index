package ro.go.adrhc.persistence.lucene.typedindex.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;

@RequiredArgsConstructor
@Getter
public class DefaultTypedIndexSearchServiceParams<T> implements TypedIndexSearchServiceParams<T> {
	private final SearchResultFilter<T> searchResultFilter;
	private final Class<T> type;
	private final TypedField<T> idField;
	private final IndexReaderPool indexReaderPool;
	private final int numHits;
}
