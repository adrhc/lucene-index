package ro.go.adrhc.persistence.lucene.operations.search;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import ro.go.adrhc.persistence.lucene.core.typed.read.ScoreAndValue;

public record QueryAndScoreAndValue<T>(Query query, ScoreAndValue<T> scoreAndValue) {
	public ScoreDoc scoreDoc() {
		return scoreAndValue.scoreDoc();
	}

	public T value() {
		return scoreAndValue.value();
	}
}
