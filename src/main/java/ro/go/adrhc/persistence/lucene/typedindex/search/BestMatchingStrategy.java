package ro.go.adrhc.persistence.lucene.typedindex.search;

import org.apache.lucene.search.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface BestMatchingStrategy<T> {
	Optional<CriterionScoreAndTyped<Query, T>>
	bestMatch(Stream<CriterionScoreAndTyped<Query, T>> findings);
}
