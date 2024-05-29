package ro.go.adrhc.persistence.lucene.typedcore;

public interface Mergeable<T> {
	T merge(T another);
}
