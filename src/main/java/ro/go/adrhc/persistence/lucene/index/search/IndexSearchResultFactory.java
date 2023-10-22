package ro.go.adrhc.persistence.lucene.index.search;

import ro.go.adrhc.persistence.lucene.index.core.read.ScoreAndDocument;

public interface IndexSearchResultFactory<S, F> {
	F create(S searchedAudio, ScoreAndDocument scoreAndDocument);
}
