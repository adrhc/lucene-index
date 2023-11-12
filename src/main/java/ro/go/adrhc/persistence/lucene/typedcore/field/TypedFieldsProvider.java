package ro.go.adrhc.persistence.lucene.typedcore.field;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Field;
import ro.go.adrhc.persistence.lucene.core.field.FieldFactory;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TypedFieldsProvider<T> {
	private final FieldFactory fieldFactory;
	private final Collection<? extends TypedField<T>> typedFields;

	public static <T, E extends Enum<E> & TypedField<T>>
	TypedFieldsProvider<T> create(TypedFieldsProviderParams<T, E> providerParams) {
		return new TypedFieldsProvider<>(
				FieldFactory.create(providerParams.getAnalyzer()),
				EnumSet.allOf(providerParams.getTFieldEnumClass()));
	}

	public Stream<Field> createFields(T tValue) {
		return typedFields.stream()
				.map(typedField -> createField(tValue, typedField))
				.flatMap(Optional::stream);
	}

	public Optional<Field> createField(T t, TypedField<T> typedField) {
		Object fieldValue = typedField.accessor().apply(t);
		if (fieldValue == null) {
			return Optional.empty();
		}
		return Optional.of(doCreate(typedField, fieldValue));
	}

	private Field doCreate(TypedField<?> typedField, Object fieldValue) {
		return fieldFactory.create(typedField.isIdField(),
				typedField.fieldType(), typedField.name(), fieldValue);
	}
}
