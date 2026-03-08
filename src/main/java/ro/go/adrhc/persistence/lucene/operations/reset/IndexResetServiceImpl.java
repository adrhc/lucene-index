package ro.go.adrhc.persistence.lucene.operations.reset;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexReset;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexWriterParams;

import java.io.IOException;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class IndexResetServiceImpl<T> implements IndexResetService<T> {
	private final TypedIndexReset<T> typedIndexReset;

	/**
	 * constructor parameters union
	 */
	public static <T> IndexResetServiceImpl<T> create(TypedIndexWriterParams<T> params) {
		return new IndexResetServiceImpl<>(TypedIndexReset.create(params));
	}

	public void reset(Stream<T> stateAfterReset) throws IOException {
		typedIndexReset.reset(stateAfterReset);
	}
}
