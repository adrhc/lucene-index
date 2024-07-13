package ro.go.adrhc.persistence.lucene.typedcore.write;

import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;

public interface TypedIndexRemoverParams {
	LuceneFieldSpec<?> getIdField();

	IndexWriter getIndexWriter();
}
