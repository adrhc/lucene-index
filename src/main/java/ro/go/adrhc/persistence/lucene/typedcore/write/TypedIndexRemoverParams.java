package ro.go.adrhc.persistence.lucene.typedcore.write;

import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;

public interface TypedIndexRemoverParams {
	TypedField<?> getIdField();

	IndexWriter getIndexWriter();
}
