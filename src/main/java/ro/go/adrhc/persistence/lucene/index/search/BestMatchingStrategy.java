package ro.go.adrhc.persistence.lucene.index.search;

import java.util.Optional;
import java.util.stream.Stream;

public interface BestMatchingStrategy<F> {
	Optional<F> bestMatch(Stream<F> findings);
}