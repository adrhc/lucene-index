package ro.go.adrhc.persistence.lucene.core.typed.read;

import org.apache.lucene.search.ScoreDoc;

public record ScoreAndValue<T>(ScoreDoc scoreDoc, T value) {
}
