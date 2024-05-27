package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.experimental.UtilityClass;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexParams;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexParamsImpl;

@UtilityClass
public class IndexRepositoryFactory {
	public static <ID, T extends Identifiable<ID>>
	IndexRepository<ID, T> create(TypedIndexParams<ID, T> context) {
		IndexOperations<ID, T> indexOperations = createIndexOperations(context);
		return new IndexRepositoryImpl<>(indexOperations, context);
	}

	public static <ID, T extends Identifiable<ID>>
	ReadOnlyIndexOperations<ID, T> createReadOnlyIndexOperations(
			TypedIndexParamsImpl<ID, T> context) {
		return create(context);
	}

	public static <ID, T extends Identifiable<ID>> IndexOperations<ID, T>
	createIndexOperations(TypedIndexParams<ID, T> params) {
		return DefaultIndexOperationsBuilder.of(params).build();
	}
}
