package ro.go.adrhc.persistence.lucene.core.typed.write;

import org.apache.lucene.index.IndexWriter;

/**
 * Used by TypedIndexAdder.
 */
public interface TypedIndexWriterParams<T> extends TypedToDocumentConverterParams<T> {
	IndexWriter indexWriter();
}
