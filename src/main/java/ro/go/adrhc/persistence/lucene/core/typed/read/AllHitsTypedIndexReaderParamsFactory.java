package ro.go.adrhc.persistence.lucene.core.typed.read;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

import static ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParamsImpl.allHits;

/**
 * It overlaps with HitsLimitedIndexReaderParams but is intended to be inherited by
 * other factories which manage their own numHits which they don't want to
 * clash with the HitsLimitedIndexReaderParams' getNumHits().
 */
@RequiredArgsConstructor
@Getter
public class AllHitsTypedIndexReaderParamsFactory<T> {
	protected final Class<T> type;
	protected final LuceneFieldSpec<T> idField;
	protected final IndexReaderPool indexReaderPool;

	public HitsLimitedIndexReaderParams<T> allHitsTypedIndexReaderParams() {
		return allHits(type, idField, indexReaderPool);
	}
}
