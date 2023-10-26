package ro.go.adrhc.persistence.lucene.typedindex.domain.field.spec;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field;
import ro.go.adrhc.persistence.lucene.index.domain.field.LuceneFieldFactory;

@RequiredArgsConstructor
public class TypedLuceneFieldFactory {
	private final LuceneFieldFactory luceneFieldFactory;

	public static TypedLuceneFieldFactory create(Analyzer analyzer) {
		return new TypedLuceneFieldFactory(LuceneFieldFactory.create(analyzer));
	}

	public <T> Field create(T t, TypedFieldSpec<T> typedFieldSpec) {
		Object value = typedFieldSpec.value(t);
		return switch (typedFieldSpec.type()) {
			case IDENTIFIER -> LuceneFieldFactory.keywordField(typedFieldSpec.field(), value);
			case LONG -> LuceneFieldFactory.longField(typedFieldSpec.field(), (Long) value);
			case PHRASE -> LuceneFieldFactory.textField(typedFieldSpec.field(), value);
			case WORD -> luceneFieldFactory.stringField(typedFieldSpec.field(), value);
		};
	}
}
