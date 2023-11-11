package ro.go.adrhc.persistence.lucene.index.album;

import org.apache.lucene.search.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.core.read.DocumentsIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.count.DocumentsCountService;
import ro.go.adrhc.persistence.lucene.typedindex.*;
import ro.go.adrhc.persistence.lucene.typedindex.core.indexds.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.QuerySearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.SearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static ro.go.adrhc.persistence.lucene.index.TestIndexContext.createTypedIndexSpec;
import static ro.go.adrhc.persistence.lucene.index.album.AlbumsGenerator.ALBUMS;
import static ro.go.adrhc.persistence.lucene.typedindex.core.indexds.IndexDataSourceFactory.createCachedDataSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractAlbumsIndexTest {
	@TempDir
	protected static Path tmpDir;
	protected TypedIndexContext<String, Album, AlbumFieldType> albumsIndexSpec;
	protected TypedIndexFactories<String, Album, AlbumFieldType> albumsIndexFactories;

	@BeforeAll
	void beforeAll() throws IOException {
		albumsIndexSpec = createTypedIndexSpec(Album.class, AlbumFieldType.class, tmpDir);
		albumsIndexFactories = new TypedIndexFactories<>(albumsIndexSpec);
		createCreateService().createOrReplace(ALBUMS);
	}

	@AfterAll
	void afterAll() throws IOException {
		albumsIndexFactories.close();
	}

	protected int count(Query query) throws IOException {
		return createCountService().count(query);
	}

	protected List<Album> findAllMatches(Query query) throws IOException {
		return createSearchService()
				.findAllMatches(query)
				.stream()
				.map(SearchResult::getFound)
				.toList();
	}

	protected TypedIndexSearchService<QuerySearchResult<Album>> createSearchService() {
		return albumsIndexFactories.createSearchService();
	}

	protected DocumentsCountService createCountService() {
		return albumsIndexFactories.createCountService();
	}

	protected TypedIndexUpdateService<Album> createUpdateService() {
		return albumsIndexFactories.createUpdateService();
	}

	protected TypedIndexRemoveService<String> createRemoveService() {
		return albumsIndexFactories.createRemoveService();
	}

	protected TypedIndexRestoreService<String, Album> createRestoreService() {
		return albumsIndexFactories.createRestoreService();
	}

	protected TypedSearchByIdService<String, Album> createSearchByIdService() {
		return albumsIndexFactories.createSearchByIdService();
	}

	protected TypedIndexCreateService<Album> createCreateService() {
		return albumsIndexFactories.createCreateService();
	}

	protected DocumentsIndexReaderTemplate createDocumentsIndexReaderTemplate() {
		return DocumentsIndexReaderTemplate.create(albumsIndexSpec);
	}

	protected IndexDataSource<String, Album> createAlbumsDataSource() {
		return createCachedDataSource(ALBUMS);
	}
}
