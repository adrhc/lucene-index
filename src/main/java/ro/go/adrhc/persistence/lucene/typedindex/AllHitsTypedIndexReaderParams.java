package ro.go.adrhc.persistence.lucene.typedindex;

import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParamsImpl;

public class AllHitsTypedIndexReaderParams<T> extends TypedIndexReaderParamsImpl<T> {
	public AllHitsTypedIndexReaderParams(Class<T> type,
			LuceneFieldSpec<T> idField, IndexReaderPool indexReaderPool) {
		super(type, idField, indexReaderPool, Integer.MAX_VALUE);
	}

	public AllHitsTypedIndexReaderParams<T> allHitsTypedIndexReaderParams() {
		return this;
	}
}
