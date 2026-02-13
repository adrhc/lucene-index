package ro.go.adrhc.persistence.lucene.operations.search;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import ro.go.adrhc.persistence.lucene.core.bare.read.ScoreDocAndValue;

public record QueryAndScoreAndValue<T>(Query query, ScoreDocAndValue<T> scoreAndValue) {
	public ScoreDoc scoreDoc() {
		return scoreAndValue.scoreDoc();
	}

	public T value() {
		return scoreAndValue.value();
	}
}
