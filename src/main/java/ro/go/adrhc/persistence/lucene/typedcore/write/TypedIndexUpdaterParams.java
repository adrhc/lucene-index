package ro.go.adrhc.persistence.lucene.typedcore.write;

import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedFieldsProviderParams;

public interface TypedIndexUpdaterParams<T> extends TypedFieldsProviderParams<T> {
	TypedField<T> getIdField();

	IndexWriter getIndexWriter();
}
