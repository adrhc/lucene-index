package ro.go.adrhc.persistence.lucene.index.album;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.album.Album.toId;
import static ro.go.adrhc.persistence.lucene.index.album.AlbumFieldType.ID_QUERIES;
import static ro.go.adrhc.persistence.lucene.index.album.AlbumsGenerator.ALBUMS;
import static ro.go.adrhc.persistence.lucene.index.album.AlbumsGenerator.generateAlbum;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class AlbumsCrudOperationsTest extends AbstractAlbumsIndexTest {
	@Test
	void crudTest() throws IOException {
		int count = count(ID_QUERIES.startsWith(toId("/albums/album")));
		log.info("\ncount: {}", count);
		assertThat(count).isEqualTo(ALBUMS.size());

		TypedIndexUpdateService<Album> indexUpdateService = createUpdateService();
		indexUpdateService.add(generateAlbum(4));

		TypedSearchByIdService<String, Album> searchByIdService = createSearchByIdService();
		assertThat(searchByIdService.findById(toId("/albums/album4"))).isPresent();

		TypedIndexRemoveService<String> indexRemoveService = createRemoveService();
		indexRemoveService.removeById(toId("/albums/album4"));
		assertThat(searchByIdService.findById(toId("/albums/album4"))).isEmpty();
	}

	@Test
	void updateTest() throws IOException {
		TypedSearchByIdService<String, Album> searchByIdService = createSearchByIdService();
		Optional<Album> optionalAlbum = searchByIdService.findById(toId("/albums/album1"));
		assertThat(optionalAlbum).isPresent();

		// restoring (Path) id
		// Path is badly (JSON) serialized, see:
		// https://stackoverflow.com/questions/40557821/jackson-2-incorrectly-serializing-java-java-nio-file-path
		Album originalAlbum = optionalAlbum.get();
		originalAlbum = originalAlbum.path("/albums/album1");

		String newStoredOnlyField = Instant.now().toString();
		Album album = originalAlbum.storedOnlyField(newStoredOnlyField);
		createUpdateService().update(album);

		optionalAlbum = searchByIdService.findById(album.getId());
		assertThat(optionalAlbum).isPresent();
		assertThat(optionalAlbum.get().storedOnlyField()).isEqualTo(newStoredOnlyField);
	}
}
