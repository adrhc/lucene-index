package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.experimental.UtilityClass;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexParams;

@UtilityClass
public class IndexRepositoryFactory {
	public static <ID, T extends Identifiable<ID>>
	IndexRepository<ID, T> create(TypedIndexParams<ID, T> params) {
		IndexOperations<ID, T> indexOperations = createIndexOperations(params);
		return new IndexRepositoryImpl<>(indexOperations, params);
	}

	public static <ID, T extends Identifiable<ID>> ReadOnlyIndexOperations<ID, T>
	createReadOnlyIndexOperations(TypedIndexParams<ID, T> params) {
		return create(params);
	}

	public static <ID, T extends Identifiable<ID>> IndexOperations<ID, T>
	createIndexOperations(TypedIndexParams<ID, T> params) {
		return DefaultIndexOperationsBuilder.of(params).build();
	}
}
