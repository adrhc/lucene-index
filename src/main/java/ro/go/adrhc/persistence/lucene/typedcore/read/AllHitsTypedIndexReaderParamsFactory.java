package ro.go.adrhc.persistence.lucene.typedcore.read;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;

import static ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParamsImpl.allHits;

/**
 * It overlaps with TypedIndexReaderParams but is intended to be inherited by
 * other factories which manage their own numHits which they don't want to
 * clash with the TypedIndexReaderParams' getNumHits().
 */
@RequiredArgsConstructor
@Getter
public class AllHitsTypedIndexReaderParamsFactory<T> {
	protected final Class<T> type;
	protected final LuceneFieldSpec<T> idField;
	protected final IndexReaderPool indexReaderPool;

	public TypedIndexReaderParams<T> allHitsTypedIndexReaderParams() {
		return allHits(type, idField, indexReaderPool);
	}
}
