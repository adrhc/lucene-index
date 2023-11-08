package ro.go.adrhc.persistence.lucene.index.search;

import org.apache.lucene.search.Query;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IndexSearchService<F> {
	List<F> findAllMatches(Query query) throws IOException;

	Optional<F> findBestMatch(Query query) throws IOException;

	List<F> findBestMatches(Collection<? extends Query> queries) throws IOException;

	Optional<F> findBestMatch(BestMatchingStrategy<F> bestMatchingStrategy, Query query) throws IOException;

	List<F> findBestMatches(BestMatchingStrategy<F> bestMatchingStrategy,
			Collection<? extends Query> queries) throws IOException;
}
