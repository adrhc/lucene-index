package ro.go.adrhc.persistence.lucene.index.album;

import org.apache.lucene.search.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.core.read.DocumentsIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.DocumentsCountService;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedIndexAdderService;
import ro.go.adrhc.persistence.lucene.typedindex.create.TypedIndexCreateService;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactories;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactoriesParams;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedIndexUpdateService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static ro.go.adrhc.persistence.lucene.index.TestIndexContext.createTypedIndexSpec;
import static ro.go.adrhc.persistence.lucene.index.album.AlbumsGenerator.ALBUMS;
import static ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSourceFactory.createCachedDataSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractAlbumsIndexTest {
	@TempDir
	protected static Path tmpDir;
	protected TypedIndexFactoriesParams<Path, Album, AlbumFieldType> albumsIndexSpec;
	protected TypedIndexFactories<Path, Album, AlbumFieldType> albumsIndexFactories;

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
		return createSearchService().findAllMatches(query);
	}

	protected TypedIndexSearchService<Album> createSearchService() {
		return albumsIndexFactories.createSearchService();
	}

	protected DocumentsCountService createCountService() {
		return albumsIndexFactories.createCountService();
	}

	protected TypedIndexAdderService<Album> createAdderService() {
		return albumsIndexFactories.createAdderService();
	}

	protected TypedIndexUpdateService<Album> createUpdateService() {
		return albumsIndexFactories.createUpdateService();
	}

	protected TypedIndexRemoveService<Path> createRemoveService() {
		return albumsIndexFactories.createRemoveService();
	}

	protected TypedIndexRestoreService<Path, Album> createRestoreService() {
		return albumsIndexFactories.createRestoreService();
	}

	protected TypedSearchByIdService<Path, Album> createSearchByIdService() {
		return albumsIndexFactories.createSearchByIdService();
	}

	protected TypedIndexCreateService<Path, Album> createCreateService() {
		return albumsIndexFactories.createCreateService();
	}

	protected DocumentsIndexReaderTemplate createDocumentsIndexReaderTemplate() {
		return DocumentsIndexReaderTemplate.create(albumsIndexSpec);
	}

	protected IndexDataSource<Path, Album> createAlbumsDataSource() {
		return createCachedDataSource(ALBUMS);
	}
}
