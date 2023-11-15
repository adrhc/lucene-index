package ro.go.adrhc.persistence.lucene.typedindex.search;

import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreAndValue;

public record TypedSearchResult<T>(Query query, ScoreAndValue<T> scoreAndValue) {
	public float score() {
		return scoreAndValue.score();
	}

	public T tValue() {
		return scoreAndValue.value();
	}
}
