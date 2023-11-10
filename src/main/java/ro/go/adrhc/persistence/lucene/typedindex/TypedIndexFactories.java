package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentsIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.count.DocumentsCountService;
import ro.go.adrhc.persistence.lucene.typedindex.domain.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.QuerySearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.QuerySearchResultFactory;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.search.QuerySearchResultFilter;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;

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
public class TypedIndexFactories<ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>> implements Closeable {
	private final TypedIndexResources<ID, T, E> typedIndexResources;
	private final QuerySearchResultFilter<T> searchResultFilter;

	public TypedIndexSearchService<QuerySearchResult<T>> createSearchService() {
		return new TypedIndexSearchService<>(
				DocumentsIndexReaderTemplate.create(typedIndexResources),
				QuerySearchResultFactory.create(typedIndexResources.getType()),
				searchResultFilter
		);
	}

	public TypedSearchByIdService<ID, T> createSearchByIdService() {
		return TypedSearchByIdService.create(typedIndexResources);
	}

	public DocumentsCountService createCountService() {
		return DocumentsCountService.create(typedIndexResources);
	}

	public TypedIndexRestoreService<ID, T> createRestoreService() {
		return TypedIndexRestoreService.create(typedIndexResources);
	}

	public TypedIndexCreateService<T> createCreateService() {
		return TypedIndexCreateService.create(typedIndexResources);
	}

	public TypedIndexUpdateService<T> createUpdateService() {
		return TypedIndexUpdateService.create(typedIndexResources);
	}

	public TypedIndexRemoveService<ID> createRemoveService() {
		return TypedIndexRemoveService.create(typedIndexResources);
	}

	@Override
	public void close() throws IOException {
		typedIndexResources.close();
	}
}
