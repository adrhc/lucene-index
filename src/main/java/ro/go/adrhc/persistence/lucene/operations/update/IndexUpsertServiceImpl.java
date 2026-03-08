package ro.go.adrhc.persistence.lucene.operations.update;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.typed.Identifiable;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexUpsert;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexUpsertParams;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class IndexUpsertServiceImpl<T extends Identifiable<?>> implements IndexUpsertService<T> {
	private final TypedIndexUpsert<T> typedIndexUpsert;

	public static <T extends Identifiable<?>>
	IndexUpsertServiceImpl<T> create(TypedIndexUpsertParams<T> params) {
		return new IndexUpsertServiceImpl<>(TypedIndexUpsert.create(params));
	}

	@Override
	public void upsert(T t) throws IOException {
		typedIndexUpsert.upsert(t);
	}

	@Override
	public void upsertMany(Collection<T> collection) throws IOException {
		typedIndexUpsert.upsertMany(collection);
	}
}
