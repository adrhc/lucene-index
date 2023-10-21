package ro.go.adrhc.persistence.lucene.services.search;

import ro.go.adrhc.persistence.lucene.core.read.ScoreAndDocument;

public interface IndexSearchResultFactory<S, F> {
	F create(S searchedAudio, ScoreAndDocument scoreAndDocument);
}
