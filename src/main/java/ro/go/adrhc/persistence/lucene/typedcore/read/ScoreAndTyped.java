package ro.go.adrhc.persistence.lucene.typedcore.read;

public record ScoreAndTyped<T>(float score, T tValue) {
}
