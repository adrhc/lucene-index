package ro.go.adrhc.persistence.lucene.operations.search;

import org.apache.lucene.search.Query;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SearchReduceService<T> {
	Optional<T> findBestMatch(
		BestMatchingStrategy<T> bestMatchingStrategy,
		Query query) throws IOException;

	List<QueryAndValue<T>> findBestMatches(
		BestMatchingStrategy<T> bestMatchingStrategy,
		Collection<? extends Query> queries) throws IOException;
}
