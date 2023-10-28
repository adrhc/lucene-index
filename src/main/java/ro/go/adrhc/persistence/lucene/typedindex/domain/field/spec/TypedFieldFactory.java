package ro.go.adrhc.persistence.lucene.typedindex.domain.field.spec;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field;
import ro.go.adrhc.persistence.lucene.index.domain.field.FieldFactory;

import java.util.Optional;

@RequiredArgsConstructor
public class TypedFieldFactory {
	private final FieldFactory fieldFactory;

	public static TypedFieldFactory create(Analyzer analyzer) {
		return new TypedFieldFactory(FieldFactory.create(analyzer));
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
			case KEYWORD -> FieldFactory.keywordField(typedFieldSpec.field(), value);
			case LONG -> FieldFactory.longField(typedFieldSpec.field(), (Long) value);
			case INT -> FieldFactory.intField(typedFieldSpec.field(), (Integer) value);
			case PHRASE -> FieldFactory.textField(typedFieldSpec.field(), value);
			case WORD -> fieldFactory.stringField(typedFieldSpec.field(), value);
		};
	}
}
