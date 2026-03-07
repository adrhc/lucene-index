package ro.go.adrhc.persistence.lucene.core.typed.write;

import org.apache.lucene.index.IndexWriter;

public interface TypedIndexWriterParams<T> extends TypedToDocumentConverterParams<T> {
	IndexWriter indexWriter();
}
