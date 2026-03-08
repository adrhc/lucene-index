package ro.go.adrhc.persistence.lucene.core.typed.write;

import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class TypedIndexRemoverTemplate<I> {
	private final TypedIndexRemover<I> indexRemover;

	public static <I>
	TypedIndexRemoverTemplate<I> create(TypedIndexRemoverParams params) {
		TypedIndexRemover<I> indexRemover = TypedIndexRemover.create(params);
		return new TypedIndexRemoverTemplate<>(indexRemover);
	}

	public <E extends Exception> void useRemover(
		SneakyConsumer<TypedIndexRemover<I>, E> indexRemoverConsumer)
		throws IOException, E {
		try (TypedIndexRemover<I> indexRemover = this.indexRemover) {
			indexRemoverConsumer.accept(indexRemover);
		}
	}
}
