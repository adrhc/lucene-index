package ro.go.adrhc.persistence.lucene.core.typed.field;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Field;
import ro.go.adrhc.persistence.lucene.core.bare.field.FieldFactory;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ObjectPropsToLuceneFieldsConverter<T> {
	private final FieldFactory fieldFactory;
	private final Collection<? extends LuceneFieldSpec<T>> typedFields;

	public static <T> ObjectPropsToLuceneFieldsConverter<T>
	create(ObjectPropsToLuceneFieldsConverterParams<T> params) {
		return new ObjectPropsToLuceneFieldsConverter<>(
				FieldFactory.create(params.getAnalyzer()),
				params.getTypedFields());
	}

	public Stream<Field> toFields(T tValue) {
		return typedFields.stream()
				.map(typedField -> toField(tValue, typedField))
				.flatMap(Optional::stream);
	}

	public Optional<Field> toField(T t, LuceneFieldSpec<T> typedField) {
		Object fieldValue = typedField.typedToIndexableValue(t);
		if (fieldValue == null) {
			return Optional.empty();
		} else {
			return Optional.of(toField(typedField, fieldValue));
		}
	}

	private Field toField(LuceneFieldSpec<?> typedField, Object fieldValue) {
		return fieldFactory.create(typedField.mustStore(),
				typedField.fieldType(), typedField.name(), fieldValue);
	}
}
