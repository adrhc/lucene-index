package ro.go.adrhc.persistence.lucene.core.typed.write;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

import java.util.Collection;

public record TypedIndexWriterParamsImpl<T>(IndexWriter indexWriter, Analyzer analyzer,
	Collection<? extends LuceneFieldSpec<T>> typedFields) implements TypedIndexWriterParams<T> {
}
