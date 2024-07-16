package ro.go.adrhc.persistence.lucene.album;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.FileSystemIndex;
import ro.go.adrhc.persistence.lucene.FileSystemIndexImpl;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServicesParamsFactory;

import java.io.IOException;
import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.TypedIndexParamsTestFactory.createTypedIndexSpec;
import static ro.go.adrhc.persistence.lucene.album.AlbumsGenerator.ALBUMS;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractAlbumsIndexTest {
	@TempDir
	protected static Path tmpDir;
	protected IndexServicesParamsFactory<Album> albumsIndexSpec;
	protected FileSystemIndex<Path, Album> indexRepository;

	@BeforeAll
	void beforeAll() throws IOException {
		albumsIndexSpec = createTypedIndexSpec(Album.class, AlbumFieldType.class, tmpDir);
		indexRepository = FileSystemIndexImpl.of(albumsIndexSpec);
		indexRepository.reset(ALBUMS);
	}

	@AfterAll
	void afterAll() throws IOException {
		albumsIndexSpec.close();
	}
}
