package ro.go.adrhc.persistence.lucene.typedindex.search;

import java.util.Optional;
import java.util.stream.Stream;

public interface BestMatchingStrategy<T> {
	Optional<TypedSearchResult<T>> bestMatch(Stream<TypedSearchResult<T>> findings);
}
