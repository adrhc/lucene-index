package ro.go.adrhc.persistence.lucene.core.typed.write.shallow;

import lombok.experimental.UtilityClass;
import ro.go.adrhc.persistence.lucene.core.typed.Identifiable;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class TypedIndexDataSourceFactory {
	public static <I, T extends Identifiable<I>>
	TypedIndexDataSource<I, T> createCachedDataSource(Stream<T> tCollection) {
		return createCachedDataSource(tCollection.toList());
	}

	public static <I, T extends Identifiable<I>>
	TypedIndexDataSource<I, T> createCachedDataSource(Collection<T> tCollection) {
		return new TypedIndexDataSource<>() {
			@Override
			public Stream<I> loadAllIds() {
				return tCollection.stream().map(Identifiable::getId);
			}

			@Override
			public Stream<T> loadByIds(Stream<I> idStream) {
				Set<I> ids = idStream.collect(Collectors.toSet());
				return loadAll().filter(t -> ids.contains(t.getId()));
			}

			@Override
			public Stream<T> loadAll() {
				return tCollection.stream();
			}
		};
	}
}
