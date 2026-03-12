package ro.go.adrhc.persistence.lucene.album;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.LuceneIndex;
import ro.go.adrhc.persistence.lucene.LuceneIndexImpl;
import ro.go.adrhc.persistence.lucene.operation.IndexOperationsParams;

import java.io.IOException;
import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.IndexOperationsParamsGenerator.createFSIndexParams;
import static ro.go.adrhc.persistence.lucene.album.AlbumsGenerator.ALBUMS;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractAlbumsIndexTest {
	@TempDir
	protected static Path tmpDir;
	protected IndexOperationsParams<Album> albumsIndexSpec;
	protected LuceneIndex<Path, Album> indexRepository;

	@BeforeAll
	void beforeAll() throws IOException {
		albumsIndexSpec = createFSIndexParams(Album.class, AlbumSchema.class, tmpDir);
		indexRepository = LuceneIndexImpl.of(albumsIndexSpec);
		indexRepository.reset(ALBUMS);
	}

	@AfterAll
	void afterAll() throws IOException {
		albumsIndexSpec.close();
	}
}
