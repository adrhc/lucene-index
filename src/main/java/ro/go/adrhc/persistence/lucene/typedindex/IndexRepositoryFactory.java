package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.experimental.UtilityClass;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;

@UtilityClass
public class IndexRepositoryFactory {
	public static <ID, T extends Identifiable<ID>>
	IndexRepository<ID, T> create(TypedIndexContext<T> context) {
		IndexOperations<ID, T> indexOperations = IndexOperationsFactory.create(context);
		return new IndexRepositoryImpl<>(indexOperations, context);
	}
}
