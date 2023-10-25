package ro.go.adrhc.persistence.lucene.index.search;

import ro.go.adrhc.persistence.lucene.index.core.read.ScoreAndDocument;

import java.util.Optional;

public interface IndexSearchResultFactory<S, F> {
	Optional<F> create(S searched, ScoreAndDocument scoreAndDocument);
}
