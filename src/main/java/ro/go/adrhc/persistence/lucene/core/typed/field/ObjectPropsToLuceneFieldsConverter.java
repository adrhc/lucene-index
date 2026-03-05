package ro.go.adrhc.persistence.lucene.core.typed.field;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Field;
import ro.go.adrhc.persistence.lucene.core.bare.field.FieldFactory;

import java.util.Collection;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ObjectPropsToLuceneFieldsConverter<T> {
	private final Collection<? extends LuceneFieldSpec<T>> typedFields;

	public static <T> ObjectPropsToLuceneFieldsConverter<T>
	create(ObjectPropsToLuceneFieldsConverterParams<T> params) {
		return new ObjectPropsToLuceneFieldsConverter<>(params.typedFields());
	}

	public Stream<Field> toFields(T tValue) {
		return typedFields.stream().flatMap(typedField -> toFields(tValue, typedField));
	}

	protected Stream<Field> toFields(T t, LuceneFieldSpec<T> typedField) {
		Object indexableValue = typedField.typedToIndexableValue(t);
		if (indexableValue == null) {
			return Stream.empty();
		} else if (indexableValue instanceof Collection<?> col) {
			return col.stream().map(value -> FieldFactory.create(typedField, value));
		} else if (typedField.supportsSorting()) {
			return Stream.of(FieldFactory.create(typedField, indexableValue),
				FieldFactory.createSortField(typedField, indexableValue));
		} else {
			return Stream.of(FieldFactory.create(typedField, indexableValue));
		}
	}
}
