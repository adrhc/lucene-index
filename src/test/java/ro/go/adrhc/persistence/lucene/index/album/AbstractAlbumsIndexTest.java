package ro.go.adrhc.persistence.lucene.index.album;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.typedindex.FSIndexRepository;
import ro.go.adrhc.persistence.lucene.typedindex.FSIndexRepositoryImpl;
import ro.go.adrhc.persistence.lucene.typedindex.srvparams.IndexServicesParamsFactory;

import java.io.IOException;
import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.index.TypedIndexParamsTestFactory.createTypedIndexSpec;
import static ro.go.adrhc.persistence.lucene.index.album.AlbumsGenerator.ALBUMS;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractAlbumsIndexTest {
	@TempDir
	protected static Path tmpDir;
	protected IndexServicesParamsFactory<Album> albumsIndexSpec;
	protected FSIndexRepository<Path, Album> indexRepository;

	@BeforeAll
	void beforeAll() throws IOException {
		albumsIndexSpec = createTypedIndexSpec(Album.class, AlbumFieldType.class, tmpDir);
		indexRepository = FSIndexRepositoryImpl.of(albumsIndexSpec);
		indexRepository.reset(ALBUMS);
	}

	@AfterAll
	void afterAll() throws IOException {
		albumsIndexSpec.close();
	}
}
