package ro.go.adrhc.persistence.lucene.core.typed;

public interface Mergeable<T> {
	T merge(T another);
}
