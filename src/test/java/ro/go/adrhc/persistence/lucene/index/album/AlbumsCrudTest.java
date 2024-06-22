package ro.go.adrhc.persistence.lucene.index.album;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.album.AlbumFieldType.ID_QUERIES;
import static ro.go.adrhc.persistence.lucene.index.album.AlbumsGenerator.ALBUMS;
import static ro.go.adrhc.persistence.lucene.index.album.AlbumsGenerator.generateAlbum;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class AlbumsCrudTest extends AbstractAlbumsIndexTest {
	@Test
	void crudTest() throws IOException {
		int count = indexRepository.count(ID_QUERIES
				.startsWith(Path.of("/albums/album").toString()));
		log.info("\ncount: {}", count);
		assertThat(count).isEqualTo(ALBUMS.size());

		Album album4 = generateAlbum(4);
		indexRepository.addOne(album4);
		assertThat(indexRepository.findById(album4.id())).isPresent();

		Album album4Updated = album4.storedOnlyField("updated album4 storedOnlyField");
		indexRepository.upsert(album4Updated);
		Optional<Album> optionalAlbum = indexRepository.findById(album4.id());
		assertThat(optionalAlbum).isPresent();
		assertThat(optionalAlbum.get()).isEqualTo(album4Updated);

		indexRepository.removeById(album4.id());
		assertThat(indexRepository.findById(album4.id())).isEmpty();
	}

	@Test
	void findByIdsTest() throws IOException {
		Set<Album> result = indexRepository
				.findByIds(ALBUMS.stream().map(Album::id).collect(Collectors.toSet()));
		assertThat(result).hasSize(ALBUMS.size());
	}

	@Test
	void updateTest() throws IOException {
		Optional<Album> optionalAlbum = indexRepository.findById(Path.of("/albums/album1"));
		assertThat(optionalAlbum).isPresent();

		String newStoredOnlyField = Instant.now().toString();
		Album album = optionalAlbum.get().storedOnlyField(newStoredOnlyField);
		indexRepository.upsert(album);

		optionalAlbum = indexRepository.findById(album.getId());
		assertThat(optionalAlbum).isPresent();
		assertThat(optionalAlbum.get().storedOnlyField()).isEqualTo(newStoredOnlyField);
	}

	@Test
	void upsertManyTest() throws IOException {
		Optional<Album> optionalAlbum1 = indexRepository.findById(Path.of("/albums/album1"));
		assertThat(optionalAlbum1).isPresent();
		Optional<Album> optionalAlbum2 = indexRepository.findById(Path.of("/albums/album2"));
		assertThat(optionalAlbum2).isPresent();
		Album album4 = generateAlbum(ALBUMS.size() + 1);

		String newStoredOnlyField = Instant.now().toString();
		Album album1 = optionalAlbum1.get().storedOnlyField(newStoredOnlyField);
		Album album2 = optionalAlbum2.get().storedOnlyField(newStoredOnlyField);
		indexRepository.upsertMany(List.of(album1, album2, album4));

		Optional<Album> optionalAlbum = indexRepository.findById(album1.getId());
		assertThat(optionalAlbum).isPresent();
		assertThat(optionalAlbum.get().storedOnlyField()).isEqualTo(newStoredOnlyField);

		optionalAlbum = indexRepository.findById(album2.getId());
		assertThat(optionalAlbum).isPresent();
		assertThat(optionalAlbum.get().storedOnlyField()).isEqualTo(newStoredOnlyField);

		optionalAlbum = indexRepository.findById(album4.getId());
		assertThat(optionalAlbum).isPresent();
		assertThat(optionalAlbum.get().storedOnlyField()).isEqualTo(album4.storedOnlyField());

		indexRepository.removeById(album4.id());
	}

	@Test
	void mergeTest() throws IOException {
		Album album4 = generateAlbum(ALBUMS.size() + 1);
		indexRepository.addOne(album4);
		assertThat(indexRepository.findById(album4.id())).isPresent();

		Album merged = new Album(album4.id(), null, "storedOnlyField-merged");
		indexRepository.merge(merged);

		Optional<Album> storedOptional = indexRepository.findById(album4.id());
		assertThat(storedOptional).isPresent();
		Album stored = storedOptional.get();
		assertThat(stored.storedOnlyField()).isEqualTo("storedOnlyField-merged");
		assertThat(stored.name()).isEqualTo(album4.name());

		indexRepository.removeById(album4.id());
		assertThat(indexRepository.findById(album4.id())).isEmpty();
	}
}
