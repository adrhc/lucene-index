package ro.go.adrhc.persistence.lucene.typedindex.create;

import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexAdderParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemoverParams;

public interface TypedIndexInitServiceParams<T>
		extends TypedIndexAdderParams<T>, TypedIndexRemoverParams {
}
