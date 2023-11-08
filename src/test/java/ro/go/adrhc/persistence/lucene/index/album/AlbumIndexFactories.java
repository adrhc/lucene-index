package ro.go.adrhc.persistence.lucene.index.album;

import ro.go.adrhc.persistence.lucene.index.IndexTestFactories;
import ro.go.adrhc.persistence.lucene.index.domain.queries.FieldQueries;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexCreateService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexFactories;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.QuerySearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.restore.DocumentsIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;

import java.nio.file.Path;

public class AlbumIndexFactories {
	public static final FieldQueries NAME_QUERIES = FieldQueries.create(AlbumFieldType.name);
	public static final FieldQueries ID_QUERIES = FieldQueries.create(AlbumFieldType.id);

	/**
	 * BestMatchingStrategy: Stream::findFirst
	 * QuerySearchResultFilter: it -> true (aka no filter)
	 */
	public static TypedIndexSearchService<QuerySearchResult<Album>>
	createSearchService(Path indexPath) {
		return createTypedIndexFactories()
				.createTypedIndexSearchService(it -> true, indexPath);
	}

	public static TypedSearchByIdService<String, Album> createSearchByIdService(Path indexPath) {
		return createTypedIndexFactories().createSearchByIdService(indexPath);
	}

	public static TypedIndexCreateService<Album> createCreateService(Path indexPath) {
		return createTypedIndexFactories().createTypedIndexCreateService(indexPath);
	}

	public static TypedIndexUpdateService<Album> createUpdateService(Path indexPath) {
		return createTypedIndexFactories().createTypedIndexUpdateService(indexPath);
	}

	public static TypedIndexRemoveService<String> createIndexRemoveService(Path indexPath) {
		return createTypedIndexFactories().createIndexRemoveService(indexPath);
	}

	public static DocumentsIndexRestoreService<String, Album> createIndexRestoreService(Path indexPath) {
		return createTypedIndexFactories().createDocumentsIndexRestoreService(indexPath);
	}

	private static TypedIndexFactories<String, Album, AlbumFieldType> createTypedIndexFactories() {
		return IndexTestFactories.createTypedIndexFactories(Album.class, AlbumFieldType.class);
	}
}
