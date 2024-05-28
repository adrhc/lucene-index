package ro.go.adrhc.persistence.lucene.typedindex.update;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexUpdaterParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexUpdaterTemplate;

import java.io.IOException;

@RequiredArgsConstructor
public class TypedIndexUpsertService<T extends Identifiable<?>> implements IndexUpsertService<T> {
	private final TypedIndexUpdaterTemplate<T> indexUpdaterTemplate;

	public static <T extends Identifiable<?>>
	TypedIndexUpsertService<T> create(TypedIndexUpdaterParams<T> params) {
		return new TypedIndexUpsertService<>(TypedIndexUpdaterTemplate.create(params));
	}

	@Override
	public void upsert(T t) throws IOException {
		indexUpdaterTemplate.useUpdater(updater -> updater.update(t));
	}

	@Override
	public void upsertAll(Iterable<T> iterable) throws IOException {
		for (T t : iterable) {
			this.upsert(t);
		}
	}
}
