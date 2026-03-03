package ro.go.adrhc.persistence.lucene.core.typed.write;

import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.typed.field.ObjectPropsToLuceneFieldsConverterParams;

public interface TypedIndexWriterParams<T> extends ObjectPropsToLuceneFieldsConverterParams<T> {
	IndexWriter indexWriter();
}
