package ro.go.adrhc.persistence.lucene.typedindex.search;

import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreAndTyped;

public record TypedSearchResult<T>(Query query, ScoreAndTyped<T> scoreAndTyped) {
	public float score() {
		return scoreAndTyped.score();
	}

	public T tValue() {
		return scoreAndTyped.tValue();
	}
}
