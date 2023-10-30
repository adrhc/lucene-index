package ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class RawDataSourceFactories {
	public static <ID, T extends Identifiable<ID>>
	RawDataSource<ID, T> createCachedRawDs(Collection<T> tCollection) {
		return new RawDataSource<>() {
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
