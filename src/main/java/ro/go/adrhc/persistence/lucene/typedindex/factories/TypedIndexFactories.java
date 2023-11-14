package ro.go.adrhc.persistence.lucene.typedindex.factories;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.DocumentsCountService;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedIndexAdderService;
import ro.go.adrhc.persistence.lucene.typedindex.create.TypedIndexCreateService;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedIndexUpdateService;

import java.io.Closeable;
import java.io.IOException;

/**
 * see also ro.go.adrhc.persistence.lucene.index.count.DocumentsCountService
 *
 * @param <ID> is the index id, String and Long are permitted
 * @param <T>  a JSON (ser/deser)ializable
 * @param <E>  is the TypedField that describes the lucene Document in relation with T
 */
@RequiredArgsConstructor
public class TypedIndexFactories
		<ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
		implements Closeable {
	private final TypedIndexFactoriesParams<ID, T, E> factoriesParams;

	public TypedIndexSearchService<T> createSearchService() {
		return TypedIndexSearchService.create(factoriesParams);
	}

	public TypedSearchByIdService<ID, T> createSearchByIdService() {
		return TypedSearchByIdService.create(factoriesParams);
	}

	public DocumentsCountService createCountService() {
		return DocumentsCountService.create(factoriesParams.getIndexReaderPool());
	}

	public TypedIndexRestoreService<ID, T> createRestoreService() {
		return TypedIndexRestoreService.create(factoriesParams);
	}

	public TypedIndexCreateService<ID, T> createCreateService() {
		return TypedIndexCreateService.create(factoriesParams);
	}

	public TypedIndexAdderService<T> createAdderService() {
		return TypedIndexAdderService.create(factoriesParams);
	}

	public TypedIndexUpdateService<T> createUpdateService() {
		return TypedIndexUpdateService.create(factoriesParams);
	}

	public TypedIndexRemoveService<ID> createRemoveService() {
		return TypedIndexRemoveService.create(factoriesParams);
	}

	@Override
	public void close() throws IOException {
		factoriesParams.close();
	}
}
