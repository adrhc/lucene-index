package ro.go.adrhc.persistence.lucene.typedindex.update;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.typedcore.Identifiable;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexUpsertParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedUpsertTemplate;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class TypedUpsertService<T extends Identifiable<?>> implements IndexUpsertService<T> {
	private final TypedUpsertTemplate<T> indexUpsertTemplate;

	public static <T extends Identifiable<?>>
	TypedUpsertService<T> create(TypedIndexUpsertParams<T> params) {
		return new TypedUpsertService<>(TypedUpsertTemplate.create(params));
	}

	@Override
	public void upsert(T t) throws IOException {
		indexUpsertTemplate.useUpserter(upserter -> upserter.upsert(t));
	}

	@Override
	public void upsertMany(Collection<T> collection) throws IOException {
		indexUpsertTemplate.useUpserter(upserter -> upserter.upsertMany(collection));
	}
}
