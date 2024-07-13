package ro.go.adrhc.persistence.lucene.typedcore.write;

import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.typedcore.field.ObjectPropsToLuceneFieldsConverterParams;

public interface TypedIndexWriterParams<T> extends ObjectPropsToLuceneFieldsConverterParams<T> {
	IndexWriter getIndexWriter();
}
