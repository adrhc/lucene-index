package ro.go.adrhc.persistence.lucene.core.typed;

public interface Indexable<ID, T> extends Identifiable<ID>, Mergeable<T> {
}
