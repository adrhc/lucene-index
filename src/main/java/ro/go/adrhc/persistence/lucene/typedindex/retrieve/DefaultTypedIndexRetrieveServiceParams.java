package ro.go.adrhc.persistence.lucene.typedindex.retrieve;

import lombok.Getter;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedindex.AllHitsTypedIndexReaderParams;

@Getter
public class DefaultTypedIndexRetrieveServiceParams<T> extends
		AllHitsTypedIndexReaderParams<T> implements TypedIndexRetrieveServiceParams<T> {
	public DefaultTypedIndexRetrieveServiceParams(Class<T> type,
			TypedField<T> idField, IndexReaderPool indexReaderPool) {
		super(type, idField, indexReaderPool);
	}
}
