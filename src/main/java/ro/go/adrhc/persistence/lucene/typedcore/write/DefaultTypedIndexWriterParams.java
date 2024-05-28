package ro.go.adrhc.persistence.lucene.typedcore.write;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;

import java.util.Collection;

@RequiredArgsConstructor
@Getter
public class DefaultTypedIndexWriterParams<T> implements TypedIndexWriterParams<T> {
	private final IndexWriter indexWriter;
	private final Analyzer analyzer;
	private final Collection<? extends TypedField<T>> typedFields;
}
