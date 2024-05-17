package ro.go.adrhc.persistence.lucene.index.album;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;

import java.nio.file.Path;

public record Album(Path path, String name, String storedOnlyField) implements Identifiable<Path> {
	public Album storedOnlyField(String storedOnlyField) {
		return new Album(path, name, storedOnlyField);
	}

	@JsonIgnore
	public Path id() {
		return path;
	}
}
