package ro.go.adrhc.persistence.lucene.index.album;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentsIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.count.DocumentsCountService;
import ro.go.adrhc.persistence.lucene.typedindex.*;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.QuerySearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.SearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.restore.DocumentsIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static ro.go.adrhc.persistence.lucene.index.IndexTestFactories.createTypedIndexSpec;
import static ro.go.adrhc.persistence.lucene.index.album.AlbumsGenerator.ALBUMS;
import static ro.go.adrhc.persistence.lucene.typedindex.core.docds.DocumentsDataSourceFactory.createCached;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractAlbumsIndexTest {
	@TempDir
	protected static Path tmpDir;
	protected TypedIndexSpec<String, Album, AlbumFieldType> albumsIndexSpec;
	protected TypedIndexFactories<String, Album, AlbumFieldType> albumsIndexFactories;

	@BeforeAll
	void beforeAll() throws IOException {
		albumsIndexSpec = createTypedIndexSpec(Album.class, AlbumFieldType.class, tmpDir);
		albumsIndexFactories = new TypedIndexFactories<>(albumsIndexSpec, it -> true);
		createCreateService().createOrReplace(ALBUMS);
	}

	@AfterAll
	void afterAll() throws IOException {
		albumsIndexFactories.close();
	}

	protected int count(Query query) throws IOException {
		return createDocumentsCountService().count(query);
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

	protected DocumentsCountService createDocumentsCountService() {
		return albumsIndexFactories.createCountService();
	}

	protected TypedIndexUpdateService<Album> createUpdateService() {
		return albumsIndexFactories.createUpdateService();
	}

	protected TypedIndexRemoveService<String> createIndexRemoveService() {
		return albumsIndexFactories.createRemoveService();
	}

	protected DocumentsIndexRestoreService<String, Album> createIndexRestoreService() {
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

	protected IndexDataSource<String, Document> createIndexDataSource() {
		return createCached(albumsIndexSpec, ALBUMS);
	}
}
