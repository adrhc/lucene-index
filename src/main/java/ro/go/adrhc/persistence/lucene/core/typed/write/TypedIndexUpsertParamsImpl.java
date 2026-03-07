package ro.go.adrhc.persistence.lucene.core.typed.write;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.field.RawFieldValueSerdes;

import java.util.Collection;

public record TypedIndexUpsertParamsImpl<T>(
	LuceneFieldSpec<T> idField, IndexWriter indexWriter, Analyzer analyzer,
	Collection<? extends LuceneFieldSpec<T>> typedFields,
	RawFieldValueSerdes<T> rawFieldValueSerdes)
	implements TypedIndexUpsertParams<T> {
}
