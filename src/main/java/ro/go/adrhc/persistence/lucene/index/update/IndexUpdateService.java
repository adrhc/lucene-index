package ro.go.adrhc.persistence.lucene.index.update;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

public interface IndexUpdateService<T> {
	void add(T document) throws IOException;

	void addAll(Collection<T> documents) throws IOException;

	void addAll(Stream<T> documents) throws IOException;

	void update(T document) throws IOException;
}
