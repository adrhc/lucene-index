package ro.go.adrhc.persistence.lucene.index.album;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;

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
		int count = count(ID_QUERIES.startsWith(Path.of("/albums/album").toString()));
		log.info("\ncount: {}", count);
		assertThat(count).isEqualTo(ALBUMS.size());

		createAdderService().addOne(generateAlbum(4));

		TypedSearchByIdService<Path, Album> searchByIdService = createSearchByIdService();
		assertThat(searchByIdService.findById(Path.of("/albums/album4"))).isPresent();

		TypedIndexRemoveService<Path> indexRemoveService = createRemoveService();
		indexRemoveService.removeById(Path.of("/albums/album4"));
		assertThat(searchByIdService.findById(Path.of("/albums/album4"))).isEmpty();
	}

	@Test
	void updateTest() throws IOException {
		TypedSearchByIdService<Path, Album> searchByIdService = createSearchByIdService();
		Optional<Album> optionalAlbum = searchByIdService.findById(Path.of("/albums/album1"));
		assertThat(optionalAlbum).isPresent();

		String newStoredOnlyField = Instant.now().toString();
		Album album = optionalAlbum.get().storedOnlyField(newStoredOnlyField);
		createUpdateService().update(album);

		optionalAlbum = searchByIdService.findById(album.getId());
		assertThat(optionalAlbum).isPresent();
		assertThat(optionalAlbum.get().storedOnlyField()).isEqualTo(newStoredOnlyField);
	}
}
