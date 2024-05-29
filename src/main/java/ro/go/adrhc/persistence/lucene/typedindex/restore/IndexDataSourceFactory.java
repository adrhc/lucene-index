package ro.go.adrhc.persistence.lucene.typedindex.restore;

import lombok.experimental.UtilityClass;
import ro.go.adrhc.persistence.lucene.typedcore.Identifiable;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class IndexDataSourceFactory {
	public static <ID, T extends Identifiable<ID>>
	IndexDataSource<ID, T> createCachedDataSource(Stream<T> tCollection) {
		return createCachedDataSource(tCollection.toList());
	}

	public static <ID, T extends Identifiable<ID>>
	IndexDataSource<ID, T> createCachedDataSource(Collection<T> tCollection) {
		return new IndexDataSource<>() {
			@Override
			public Stream<ID> loadAllIds() {
				return tCollection.stream().map(Identifiable::getId);
			}

			@Override
			public Stream<T> loadByIds(Stream<ID> idStream) {
				Set<ID> ids = idStream.collect(Collectors.toSet());
				return loadAll().filter(t -> ids.contains(t.getId()));
			}

			@Override
			public Stream<T> loadAll() {
				return tCollection.stream();
			}
		};
	}
}
