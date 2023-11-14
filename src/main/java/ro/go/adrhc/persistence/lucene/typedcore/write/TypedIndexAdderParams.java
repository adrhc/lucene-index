package ro.go.adrhc.persistence.lucene.typedcore.write;

import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedFieldsProviderParams;

public interface TypedIndexAdderParams<T> extends TypedFieldsProviderParams<T> {
	IndexWriter getIndexWriter();
}
