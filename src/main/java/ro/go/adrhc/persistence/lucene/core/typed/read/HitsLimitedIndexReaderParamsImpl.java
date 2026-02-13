package ro.go.adrhc.persistence.lucene.core.typed.read;

import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

public record HitsLimitedIndexReaderParamsImpl<T>(Class<T> type, LuceneFieldSpec<T> idField,
	IndexReaderPool indexReaderPool, int numHits) implements HitsLimitedIndexReaderParams<T> {
	public static <T> HitsLimitedIndexReaderParams<T> allHits(Class<T> type,
		LuceneFieldSpec<T> idField, IndexReaderPool indexReaderPool) {
		return new HitsLimitedIndexReaderParamsImpl<>(type,
			idField, indexReaderPool, Integer.MAX_VALUE);
	}
}
