package ro.go.adrhc.persistence.lucene.typedindex.domain.rawds;

import lombok.experimental.UtilityClass;
import ro.go.adrhc.persistence.lucene.typedindex.domain.Identifiable;

import java.util.ArrayList;
import java.util.Collection;

@UtilityClass
public class RawDataSourceFactories {
	public static <ID, T extends Identifiable<ID>>
	RawDataSource<ID, T> create(Collection<T> tCollection) {
		return new RawDataSource<>() {
			@Override
			public Collection<ID> loadAllIds() {
				return new ArrayList<>(tCollection.stream().map(Identifiable::getId).toList());
			}

			@Override
			public Collection<T> loadByIds(Collection<ID> strings) {
				return new ArrayList<>(tCollection);
			}
		};
	}
}
