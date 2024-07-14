package ro.go.adrhc.persistence.lucene.core.typed.read;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

@RequiredArgsConstructor
@Getter
public class HitsLimitedIndexReaderParamsImpl<T> implements HitsLimitedIndexReaderParams<T> {
	protected final Class<T> type;
	protected final LuceneFieldSpec<T> idField;
	protected final IndexReaderPool indexReaderPool;
	protected final int numHits;

	public static <T> HitsLimitedIndexReaderParams<T> allHits(Class<T> type,
			LuceneFieldSpec<T> idField, IndexReaderPool indexReaderPool) {
		return new HitsLimitedIndexReaderParamsImpl<>(type,
				idField, indexReaderPool, Integer.MAX_VALUE);
	}
}
