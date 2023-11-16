package ro.go.adrhc.persistence.lucene.index.album;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.album.AlbumFieldType.ID_QUERIES;
import static ro.go.adrhc.persistence.lucene.index.album.AlbumsGenerator.ALBUMS;
import static ro.go.adrhc.persistence.lucene.index.album.AlbumsGenerator.generateAlbum;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class AlbumsCrudOperationsTest extends AbstractAlbumsIndexTest {
	@Test
	void crudTest() throws IOException {
		int count = indexRepository.count(ID_QUERIES
				.startsWith(Path.of("/albums/album").toString()));
		log.info("\ncount: {}", count);
		assertThat(count).isEqualTo(ALBUMS.size());

		Album album4 = generateAlbum(4);
		indexRepository.addOne(album4);
		assertThat(indexRepository.findById(Path.of("/albums/album4"))).isPresent();

		Album album4Updated = album4.storedOnlyField("updated album4 storedOnlyField");
		indexRepository.update(album4Updated);
		Optional<Album> optionalAlbum = indexRepository.findById(Path.of("/albums/album4"));
		assertThat(optionalAlbum).isPresent();
		assertThat(optionalAlbum.get()).isEqualTo(album4Updated);

		indexRepository.removeById(Path.of("/albums/album4"));
		assertThat(indexRepository.findById(Path.of("/albums/album4"))).isEmpty();
	}

	@Test
	void updateTest() throws IOException {
		Optional<Album> optionalAlbum = indexRepository.findById(Path.of("/albums/album1"));
		assertThat(optionalAlbum).isPresent();

		String newStoredOnlyField = Instant.now().toString();
		Album album = optionalAlbum.get().storedOnlyField(newStoredOnlyField);
		indexRepository.update(album);

		optionalAlbum = indexRepository.findById(album.getId());
		assertThat(optionalAlbum).isPresent();
		assertThat(optionalAlbum.get().storedOnlyField()).isEqualTo(newStoredOnlyField);
	}
}
