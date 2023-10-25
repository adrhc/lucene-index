package ro.go.adrhc.persistence.lucene.index.core.docds.rawds;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collection;

@UtilityClass
public class RawDataSourceFactories {
	public static <ID, T extends Identifiable<ID>>
	RawDataSource<ID, T> createCachedRawDs(Collection<T> tCollection) {
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
