package ro.go.adrhc.persistence.lucene.typedcore.write;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;

@RequiredArgsConstructor
@Getter
public class DefaultTypedIndexRemoverParams implements TypedIndexRemoverParams {
	private final LuceneFieldSpec<?> idField;
	private final IndexWriter indexWriter;
}
