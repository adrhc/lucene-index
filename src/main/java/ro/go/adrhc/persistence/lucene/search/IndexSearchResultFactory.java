package ro.go.adrhc.persistence.lucene.search;

import ro.go.adrhc.persistence.lucene.read.ScoreAndDocument;

public interface IndexSearchResultFactory<S, F> {
	F create(S searchedAudio, ScoreAndDocument scoreAndDocument);
}
