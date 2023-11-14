package ro.go.adrhc.persistence.lucene.typedindex.search;

import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreAndTyped;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IndexSearchService<T> {
	List<T> findAllMatches(Query query) throws IOException;

	Optional<T> findBestMatch(Query query) throws IOException;

	Optional<T> findBestMatch(
			BestMatchingStrategy<ScoreAndTyped<T>> bestMatchingStrategy,
			Query query) throws IOException;

	List<CriterionScoreAndTyped<Query, T>> findBestMatches(
			Collection<? extends Query> queries) throws IOException;

	List<CriterionScoreAndTyped<Query, T>> findBestMatches(
			BestMatchingStrategy<ScoreAndTyped<T>> bestMatchingStrategy,
			Collection<? extends Query> queries) throws IOException;
}
