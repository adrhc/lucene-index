package ro.go.adrhc.persistence.lucene.typedindex.domain.field;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Field;
import ro.go.adrhc.persistence.lucene.index.domain.field.FieldFactory;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexResources;
import ro.go.adrhc.persistence.lucene.typedindex.domain.Identifiable;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TypedFieldsProvider<T> {
	private final FieldFactory fieldFactory;
	private final Collection<? extends TypedField<T>> typedFields;

	public static <T extends Identifiable<?>, E extends Enum<E> & TypedField<T>> TypedFieldsProvider<T>
	create(TypedIndexResources<?, T, E> typedIndexResources) {
		return new TypedFieldsProvider<>(
				FieldFactory.create(typedIndexResources.getAnalyzer()),
				EnumSet.allOf(typedIndexResources.getTFieldEnumClass()));
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
