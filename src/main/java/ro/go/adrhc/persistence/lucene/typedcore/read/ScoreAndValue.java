package ro.go.adrhc.persistence.lucene.typedcore.read;

public record ScoreAndValue<T>(float score, T value) {
}
