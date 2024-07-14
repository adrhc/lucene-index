package ro.go.adrhc.persistence.lucene.album;

import ro.go.adrhc.persistence.lucene.core.TokenizationUtilsTest;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AlbumsGenerator {
	public static final List<Album> ALBUMS = List.of(
			new Album(Path.of("/albums/album1"), TokenizationUtilsTest.TEXT, "storedOnlyField1"),
			new Album(Path.of("/albums/album2"),
					"IMG-20210725-WA0029 ccc_ddd CAșț.jpeg", "storedOnlyField2"),
			new Album(Path.of("/albums/album3"),
					"Ping_Pong #name (Original)Person222 ăĂîÎșȘțȚ123", "storedOnlyField3"));

	public static List<Album> generateAlbumList(int size) {
		return IntStream.range(0, size)
				.mapToObj(AlbumsGenerator::generateAlbum)
				.toList();
	}

	public static Stream<Album> generateAlbumStream(int start, int end) {
		return IntStream.range(start, end)
				.mapToObj(AlbumsGenerator::generateAlbum);
	}

	public static Album generateAlbum(int i) {
		return generateAlbum(i, Path.of("/albums/album" + i));
	}

	public static Album generateAlbum(int i, Path path) {
		return new Album(path,
				"Ping_Pong #name (Original)Person222 ăĂîÎșȘțȚ123" + (i % 100),
				"storedOnlyField" + (i % 100));
	}
}
