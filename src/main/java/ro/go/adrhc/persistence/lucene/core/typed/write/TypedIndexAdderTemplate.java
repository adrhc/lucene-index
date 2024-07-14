package ro.go.adrhc.persistence.lucene.core.typed.write;

import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class TypedIndexAdderTemplate<T> {
	private final TypedIndexAdder<T> indexWriter;

	public static <T> TypedIndexAdderTemplate<T> create(TypedIndexWriterParams<T> params) {
		TypedIndexAdder<T> indexWriter = TypedIndexAdder.create(params);
		return new TypedIndexAdderTemplate<>(indexWriter);
	}

	public <E extends Exception> void useAdder(
			SneakyConsumer<TypedIndexAdder<T>, E> indexWriterConsumer)
			throws IOException, E {
		try (TypedIndexAdder<T> indexWriter = this.indexWriter) {
			indexWriterConsumer.accept(indexWriter);
		}
	}
}
