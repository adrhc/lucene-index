package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.experimental.UtilityClass;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexParams;

@UtilityClass
public class IndexRepositoryFactory {
	public static <ID, T extends Indexable<ID, T>>
	IndexRepository<ID, T> create(TypedIndexParams<T> params) {
		IndexOperations<ID, T> indexOperations = createIndexOperations(params);
		return new IndexRepositoryImpl<>(indexOperations, params);
	}

	public static <ID, T extends Indexable<ID, T>> ReadOnlyIndexOperations<ID, T>
	createReadOnlyIndexOperations(TypedIndexParams<T> params) {
		return create(params);
	}

	public static <ID, T extends Indexable<ID, T>> IndexOperations<ID, T>
	createIndexOperations(TypedIndexParams<T> params) {
		return DefaultIndexOperationsBuilder.of(params).build();
	}
}
