package ro.go.adrhc.persistence.lucene.typedindex.search;

import java.io.IOException;
import java.util.Optional;

public interface IdSearchService<ID, T> {
	Optional<T> findById(ID id) throws IOException;
}
