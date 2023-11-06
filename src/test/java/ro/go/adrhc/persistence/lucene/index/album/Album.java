package ro.go.adrhc.persistence.lucene.index.album;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;

import java.nio.file.Path;

public record Album(Path path, String name, String storedOnlyField) implements Identifiable<String> {
	public static String toId(String path) {
		return path == null ? null : Path.of(path).toString();
	}

	@Override
	@JsonIgnore
	public String getId() {
		return path.toString();
	}

	public Album storedOnlyField(String storedOnlyField) {
		return new Album(path, name, storedOnlyField);
	}

	public Album path(String path) {
		return new Album(Path.of(path), name, storedOnlyField);
	}
}
