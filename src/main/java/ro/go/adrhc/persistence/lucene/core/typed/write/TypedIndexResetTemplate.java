package ro.go.adrhc.persistence.lucene.core.typed.write;

import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class TypedIndexResetTemplate<T> {
	private final TypedIndexReset<T> indexReset;

	public static <T> TypedIndexResetTemplate<T> create(TypedIndexWriterParams<T> params) {
		TypedIndexReset<T> indexReset = TypedIndexReset.create(params);
		return new TypedIndexResetTemplate<>(indexReset);
	}

	public <E extends Exception> void useReset(
		SneakyConsumer<TypedIndexReset<T>, E> indexResetConsumer)
		throws IOException, E {
		try (TypedIndexReset<T> indexReset = this.indexReset) {
			indexResetConsumer.accept(indexReset);
		}
	}
}
