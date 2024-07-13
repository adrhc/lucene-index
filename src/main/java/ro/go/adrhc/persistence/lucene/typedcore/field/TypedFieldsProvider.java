package ro.go.adrhc.persistence.lucene.typedcore.field;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Field;
import ro.go.adrhc.persistence.lucene.core.field.FieldFactory;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TypedFieldsProvider<T> {
	private final FieldFactory fieldFactory;
	private final Collection<? extends TypedField<T>> typedFields;

	public static <T> TypedFieldsProvider<T>
	create(TypedFieldsProviderParams<T> providerParams) {
		return new TypedFieldsProvider<>(
				FieldFactory.create(providerParams.getAnalyzer()),
				providerParams.getTypedFields());
	}

	public Stream<Field> createFields(T tValue) {
		return typedFields.stream()
				.map(typedField -> createField(tValue, typedField))
				.flatMap(Optional::stream);
	}

	public Optional<Field> createField(T t, TypedField<T> typedField) {
		Object fieldValue = typedField.typedToIndexableValue(t);
		if (fieldValue == null) {
			return Optional.empty();
		} else {
			return Optional.of(doCreateField(typedField, fieldValue));
		}
	}

	private Field doCreateField(TypedField<?> typedField, Object fieldValue) {
		return fieldFactory.create(typedField.mustStore(),
				typedField.fieldType(), typedField.name(), fieldValue);
	}
}
