package ro.go.adrhc.persistence.lucene.typedindex.retrieve;

import lombok.Getter;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.typedcore.read.AllHitsTypedIndexReaderParamsFactory;

@Getter
public class TypedRetrieveServiceParamsImpl<T> extends
		AllHitsTypedIndexReaderParamsFactory<T> implements TypedRetrieveServiceParams<T> {
	public TypedRetrieveServiceParamsImpl(Class<T> type,
			LuceneFieldSpec<T> idField, IndexReaderPool indexReaderPool) {
		super(type, idField, indexReaderPool);
	}
}
