package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParams;

import static ro.go.adrhc.persistence.lucene.typedcore.read.DefaultTypedIndexReaderParams.allHits;

@RequiredArgsConstructor
@Getter
public class AllHitsTypedIndexReaderParams<T> {
	protected final Class<T> type;
	protected final LuceneFieldSpec<T> idField;
	protected final IndexReaderPool indexReaderPool;

	public TypedIndexReaderParams<T> toAllHitsTypedIndexReaderParams() {
		return allHits(getType(), getIdField(), getIndexReaderPool());
	}
}
