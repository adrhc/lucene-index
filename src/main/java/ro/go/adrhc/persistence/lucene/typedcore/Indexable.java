package ro.go.adrhc.persistence.lucene.typedcore;

public interface Indexable<ID, T> extends Identifiable<ID>, Mergeable<T> {
}
