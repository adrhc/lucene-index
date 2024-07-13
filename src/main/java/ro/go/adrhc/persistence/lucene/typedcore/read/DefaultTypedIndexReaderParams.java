package ro.go.adrhc.persistence.lucene.typedcore.read;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;

@RequiredArgsConstructor
@Getter
public class DefaultTypedIndexReaderParams<T> implements TypedIndexReaderParams<T> {
	private final Class<T> type;
	private final LuceneFieldSpec<T> idField;
	private final IndexReaderPool indexReaderPool;
	private final int numHits;

	public static <T> TypedIndexReaderParams<T> allHits(Class<T> type,
			LuceneFieldSpec<T> idField, IndexReaderPool indexReaderPool) {
		return new DefaultTypedIndexReaderParams<>(type,
				idField, indexReaderPool, Integer.MAX_VALUE);
	}
}
