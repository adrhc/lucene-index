package ro.go.adrhc.persistence.lucene.typedindex.search;

import java.util.Optional;
import java.util.stream.Stream;

public interface BestMatchingStrategy<T> {
	Optional<QueryAndScoreAndValue<T>> bestMatch(Stream<QueryAndScoreAndValue<T>> findings);
}
