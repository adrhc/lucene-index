package ro.go.adrhc.persistence.lucene.core.typed.read;

import org.apache.lucene.search.ScoreDoc;

public record ScoreDocAndValue<T>(ScoreDoc scoreDoc, T value) {
}
