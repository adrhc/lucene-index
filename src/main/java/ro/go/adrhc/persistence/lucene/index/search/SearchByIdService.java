package ro.go.adrhc.persistence.lucene.index.search;

import java.io.IOException;
import java.util.Optional;

public interface SearchByIdService<ID, T> {
	Optional<T> findById(ID id) throws IOException;
}
