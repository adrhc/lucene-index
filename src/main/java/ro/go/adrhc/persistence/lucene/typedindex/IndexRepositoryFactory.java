package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.experimental.UtilityClass;
import ro.go.adrhc.persistence.lucene.typedcore.Indexable;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexServicesParamsFactory;

@UtilityClass
public class IndexRepositoryFactory {
	public static <ID, T extends Indexable<ID, T>>
	IndexRepository<ID, T> create(TypedIndexServicesParamsFactory<T> params) {
		IndexOperations<ID, T> indexOperations = createIndexOperations(params);
		return new IndexRepositoryImpl<>(indexOperations, params);
	}

	public static <ID, T extends Indexable<ID, T>> ReadOnlyIndexOperations<ID, T>
	createReadOnlyIndexOperations(TypedIndexServicesParamsFactory<T> params) {
		return create(params);
	}

	public static <ID, T extends Indexable<ID, T>> IndexOperations<ID, T>
	createIndexOperations(TypedIndexServicesParamsFactory<T> params) {
		return IndexOperationsImplBuilder.of(params).build();
	}
}
