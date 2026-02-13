package ro.go.adrhc.persistence.lucene.core.typed.write;

import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class TypedIndexRemoverTemplate<ID> {
	private final TypedIndexRemover<ID> indexRemover;

	public static <ID>
	TypedIndexRemoverTemplate<ID> create(TypedIndexRemoverParams params) {
		TypedIndexRemover<ID> indexRemover = TypedIndexRemover.create(params);
		return new TypedIndexRemoverTemplate<>(indexRemover);
	}

	public <E extends Exception> void useRemover(
		SneakyConsumer<TypedIndexRemover<ID>, E> indexRemoverConsumer)
		throws IOException, E {
		try (TypedIndexRemover<ID> indexRemover = this.indexRemover) {
			indexRemoverConsumer.accept(indexRemover);
		}
	}
}
