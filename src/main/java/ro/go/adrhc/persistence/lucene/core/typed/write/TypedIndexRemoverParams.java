package ro.go.adrhc.persistence.lucene.core.typed.write;

import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

public interface TypedIndexRemoverParams {
	LuceneFieldSpec<?> getIdField();

	IndexWriter getIndexWriter();
}
