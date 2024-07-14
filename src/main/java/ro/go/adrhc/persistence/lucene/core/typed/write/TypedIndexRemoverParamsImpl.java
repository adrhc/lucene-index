package ro.go.adrhc.persistence.lucene.core.typed.write;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

@RequiredArgsConstructor
@Getter
public class TypedIndexRemoverParamsImpl implements TypedIndexRemoverParams {
	private final LuceneFieldSpec<?> idField;
	private final IndexWriter indexWriter;
}
