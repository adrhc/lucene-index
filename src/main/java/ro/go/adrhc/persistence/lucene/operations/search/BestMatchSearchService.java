package ro.go.adrhc.persistence.lucene.operations.search;

import org.apache.lucene.search.Query;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BestMatchSearchService<T> {
	Optional<T> findBestMatch(Query query) throws IOException;

	List<QueryAndValue<T>> findBestMatches(
		Collection<? extends Query> queries) throws IOException;
}
