package ro.go.adrhc.persistence.lucene.index.album;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ro.go.adrhc.persistence.lucene.typedindex.Indexable;

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
		return another;
	}
}
