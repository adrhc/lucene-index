package ro.go.adrhc.persistence.lucene.typedindex;

import ro.go.adrhc.persistence.lucene.typedcore.Identifiable;
import ro.go.adrhc.persistence.lucene.typedcore.Mergeable;

public interface Indexable<ID, T> extends Identifiable<ID>, Mergeable<T> {
}
