package ro.go.adrhc.persistence.lucene.typedindex.domain.field;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field;
import ro.go.adrhc.persistence.lucene.index.domain.field.FieldFactory;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TypedFieldsProvider<T> {
	private final FieldFactory fieldFactory;
	private final Collection<? extends TypedField<T>> typedFields;

	public static <T, E extends Enum<E> & TypedField<T>> TypedFieldsProvider<T>
	create(Analyzer analyzer, Class<E> typedFieldEnumClass) {
		return new TypedFieldsProvider<>(
				FieldFactory.create(analyzer),
				EnumSet.allOf(typedFieldEnumClass));
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
