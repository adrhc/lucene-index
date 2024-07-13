package ro.go.adrhc.persistence.lucene.typedcore.read;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;

@RequiredArgsConstructor
@Getter
public class OneHitIndexReaderParamsImpl<T> implements OneHitIndexReaderParams<T> {
	private final IndexReaderPool indexReaderPool;
	private final Class<T> type;
}
