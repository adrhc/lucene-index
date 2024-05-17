package ro.go.adrhc.persistence.lucene.index.album;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.typedindex.IndexRepository;
import ro.go.adrhc.persistence.lucene.typedindex.IndexRepositoryFactory;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexParams;

import java.io.IOException;
import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.index.TestIndexParams.createTypedIndexSpec;
import static ro.go.adrhc.persistence.lucene.index.album.AlbumsGenerator.ALBUMS;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractAlbumsIndexTest {
	@TempDir
	protected static Path tmpDir;
	protected TypedIndexParams<Album> albumsIndexSpec;
	protected IndexRepository<Path, Album> indexRepository;

	@BeforeAll
	void beforeAll() throws IOException {
		albumsIndexSpec = createTypedIndexSpec(Album.class, AlbumFieldType.class, tmpDir);
		indexRepository = IndexRepositoryFactory.create(albumsIndexSpec);
		indexRepository.reset(ALBUMS);
	}

	@AfterAll
	void afterAll() throws IOException {
		albumsIndexSpec.close();
	}
}
