package ro.go.adrhc.persistence.lucene.typedindex.search;

import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreAndTyped;

import java.util.Optional;
import java.util.stream.Stream;

public interface BestMatchingStrategy<F> {
	Optional<ScoreAndTyped<F>> bestMatch(Stream<ScoreAndTyped<F>> findings);
}
