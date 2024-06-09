package ro.go.adrhc.persistence.lucene.typedcore.write;

import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.typedcore.Identifiable;

import java.io.IOException;

@RequiredArgsConstructor
public class TypedUpsertTemplate<T extends Identifiable<?>> {
	private final TypedIndexUpsert<T> indexUpsert;

	public static <T extends Identifiable<?>>
	TypedUpsertTemplate<T> create(TypedIndexUpsertParams<T> params) {
		TypedIndexUpsert<T> indexUpdater = TypedIndexUpsert.create(params);
		return new TypedUpsertTemplate<>(indexUpdater);
	}

	public <E extends Exception> void useUpdater(
			SneakyConsumer<TypedIndexUpsert<T>, E> indexUpdaterConsumer)
			throws IOException, E {
		try (TypedIndexUpsert<T> indexUpdater = this.indexUpsert) {
			indexUpdaterConsumer.accept(indexUpdater);
		}
	}
}
