package ro.go.adrhc.persistence.lucene.core.typed.write;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

import java.util.Collection;

@RequiredArgsConstructor
@Getter
public class TypedIndexUpsertParamsImpl<T> implements TypedIndexUpsertParams<T> {
	private final LuceneFieldSpec<T> idField;
	private final IndexWriter indexWriter;
	private final Analyzer analyzer;
	private final Collection<? extends LuceneFieldSpec<T>> typedFields;
}
