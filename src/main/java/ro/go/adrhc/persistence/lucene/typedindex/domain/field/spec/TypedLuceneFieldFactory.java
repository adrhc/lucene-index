package ro.go.adrhc.persistence.lucene.typedindex.domain.field.spec;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field;
import ro.go.adrhc.persistence.lucene.index.domain.field.LuceneFieldFactory;

import java.util.Optional;

@RequiredArgsConstructor
public class TypedLuceneFieldFactory {
	private final LuceneFieldFactory luceneFieldFactory;

	public static TypedLuceneFieldFactory create(Analyzer analyzer) {
		return new TypedLuceneFieldFactory(LuceneFieldFactory.create(analyzer));
	}

	public <T> Optional<Field> create(T t, TypedFieldSpec<T> typedFieldSpec) {
		Object value = typedFieldSpec.value(t);
		if (value == null) {
			return Optional.empty();
		}
		return Optional.of(doCreate(typedFieldSpec, value));
	}

	private Field doCreate(TypedFieldSpec<?> typedFieldSpec, Object value) {
		return switch (typedFieldSpec.type()) {
			case KEYWORD -> LuceneFieldFactory.keywordField(typedFieldSpec.field(), value);
			case LONG -> LuceneFieldFactory.longField(typedFieldSpec.field(), (Long) value);
			case PHRASE -> LuceneFieldFactory.textField(typedFieldSpec.field(), value);
			case WORD -> luceneFieldFactory.stringField(typedFieldSpec.field(), value);
		};
	}
}
