package ro.go.adrhc.persistence.lucene.typedindex.factories;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.read.DocumentsIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.count.DocumentsCountService;
import ro.go.adrhc.persistence.lucene.typedcore.docserde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexCreateService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;
import ro.go.adrhc.persistence.lucene.typedindex.search.result.QuerySearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.search.result.QuerySearchResultFactory;

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

	public TypedIndexSearchService<QuerySearchResult<T>> createSearchService() {
		return new TypedIndexSearchService<>(
				DocumentsIndexReaderTemplate.create(factoriesParams),
				QuerySearchResultFactory.create(factoriesParams.getType()),
				factoriesParams.getSearchResultFilter()
		);
	}

	public TypedSearchByIdService<ID, T> createSearchByIdService() {
		return TypedSearchByIdService.create(factoriesParams);
	}

	public DocumentsCountService createCountService() {
		return DocumentsCountService.create(factoriesParams);
	}

	public TypedIndexRestoreService<ID, T> createRestoreService() {
		return TypedIndexRestoreService.create(factoriesParams);
	}

	public TypedIndexCreateService<T> createCreateService() {
		return TypedIndexCreateService.create(factoriesParams);
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
