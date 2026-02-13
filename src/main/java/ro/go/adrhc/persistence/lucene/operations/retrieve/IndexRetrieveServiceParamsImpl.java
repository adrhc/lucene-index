package ro.go.adrhc.persistence.lucene.operations.retrieve;

import lombok.Getter;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.read.AllHitsTypedIndexReaderParamsFactory;

@Getter
public class IndexRetrieveServiceParamsImpl<T> extends
	AllHitsTypedIndexReaderParamsFactory<T> implements IndexRetrieveServiceParams<T> {
	public IndexRetrieveServiceParamsImpl(Class<T> type,
		LuceneFieldSpec<T> idField, IndexReaderPool indexReaderPool) {
		super(type, idField, indexReaderPool);
	}
}
