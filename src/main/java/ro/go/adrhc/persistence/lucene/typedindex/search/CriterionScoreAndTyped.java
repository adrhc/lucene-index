package ro.go.adrhc.persistence.lucene.typedindex.search;

import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreAndTyped;

public record CriterionScoreAndTyped<C, T>(C criterion, ScoreAndTyped<T> scoreAndTyped) {
	public float score() {
		return scoreAndTyped.score();
	}

	public T tValue() {
		return scoreAndTyped.tValue();
	}
}
