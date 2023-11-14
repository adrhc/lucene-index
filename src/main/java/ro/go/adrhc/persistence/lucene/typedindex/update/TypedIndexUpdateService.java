package ro.go.adrhc.persistence.lucene.typedindex.update;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexUpdaterParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexUpdaterTemplate;

import java.io.IOException;

@RequiredArgsConstructor
public class TypedIndexUpdateService<T extends Identifiable<?>> implements IndexUpdateService<T> {
	private final TypedIndexUpdaterTemplate<T> indexUpdaterTemplate;

	public static <T extends Identifiable<?>>
	TypedIndexUpdateService<T> create(TypedIndexUpdaterParams<T> params) {
		return new TypedIndexUpdateService<>(TypedIndexUpdaterTemplate.create(params));
	}

	@Override
	public void update(T t) throws IOException {
		indexUpdaterTemplate.useUpdater(updater -> updater.update(t));
	}
}
