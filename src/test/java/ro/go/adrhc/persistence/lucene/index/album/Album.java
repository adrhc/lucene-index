package ro.go.adrhc.persistence.lucene.index.album;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ro.go.adrhc.persistence.lucene.typedcore.Indexable;

import java.nio.file.Path;

public record Album(Path path, String name, String storedOnlyField)
		implements Indexable<Path, Album> {
	public Album storedOnlyField(String storedOnlyField) {
		return new Album(path, name, storedOnlyField);
	}

	@JsonIgnore
	public Path id() {
		return path;
	}

	@Override
	public Album merge(Album another) {
		return new Album(path,
				another.name == null ? name : another.name,
				another.storedOnlyField == null ? storedOnlyField : another.storedOnlyField);
	}
}
