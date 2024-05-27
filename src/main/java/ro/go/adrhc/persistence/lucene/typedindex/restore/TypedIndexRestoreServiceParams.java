package ro.go.adrhc.persistence.lucene.typedindex.restore;

import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.AbstractTypedIndexParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemoverParams;

import java.util.function.Predicate;

public interface TypedIndexRestoreServiceParams<ID, T> extends
		TypedIndexReaderParams<T>, AbstractTypedIndexParams<T>, TypedIndexRemoverParams {
	default Predicate<ID> shouldKeep() {
		return _ -> false;
	}
}
