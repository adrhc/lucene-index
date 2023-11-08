package ro.go.adrhc.persistence.lucene.index.album;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentsIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.count.DocumentsCountService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexCreateService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.QuerySearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.SearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.restore.DocumentsIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static ro.go.adrhc.persistence.lucene.index.IndexTestFactories.ANALYZER;
import static ro.go.adrhc.persistence.lucene.index.album.AlbumsGenerator.ALBUMS;
import static ro.go.adrhc.persistence.lucene.typedindex.core.docds.DocumentsDataSourceFactory.createCached;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractAlbumsIndexTest {
	@TempDir
	protected static Path tmpDir;

	public static TypedIndexSearchService<QuerySearchResult<Album>> createIndexSearchService() {
		return AlbumIndexFactories.createSearchService(tmpDir);
	}

	@BeforeAll
	void beforeAll() throws IOException {
		createCreateService().createOrReplace(ALBUMS);
	}

	protected List<Album> findAllMatches(Query query) throws IOException {
		return createIndexSearchService()
				.findAllMatches(query)
				.stream()
				.map(SearchResult::getFound)
				.toList();
	}

	protected int count(Query query) throws IOException {
		return createDocumentsCountService().count(query);
	}

	protected DocumentsCountService createDocumentsCountService() {
		return DocumentsCountService.create(tmpDir);
	}

	protected TypedIndexUpdateService<Album> createUpdateService() {
		return AlbumIndexFactories.createUpdateService(tmpDir);
	}

	protected TypedIndexRemoveService<String> createIndexRemoveService() {
		return AlbumIndexFactories.createIndexRemoveService(tmpDir);
	}

	protected DocumentsIndexRestoreService<String, Album> createIndexRestoreService() {
		return AlbumIndexFactories.createIndexRestoreService(tmpDir);
	}

	protected TypedSearchByIdService<String, Album> createSearchByIdService() {
		return AlbumIndexFactories.createSearchByIdService(tmpDir);
	}

	protected TypedIndexCreateService<Album> createCreateService() {
		return AlbumIndexFactories.createCreateService(tmpDir);
	}

	protected DocumentsIndexReaderTemplate createDocumentsIndexReaderTemplate() {
		return DocumentsIndexReaderTemplate.create(tmpDir);
	}

	protected IndexDataSource<String, Document> createIndexDataSource() {
		return createCached(ANALYZER, AlbumFieldType.class, ALBUMS);
	}
}
