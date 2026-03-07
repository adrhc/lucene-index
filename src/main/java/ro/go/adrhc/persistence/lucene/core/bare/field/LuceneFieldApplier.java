package ro.go.adrhc.persistence.lucene.core.bare.field;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.*;
import org.apache.lucene.util.BytesRef;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

import java.util.Collection;
import java.util.function.Supplier;

import static ro.go.adrhc.persistence.lucene.core.bare.field.FieldFactory.*;

@RequiredArgsConstructor
public class LuceneFieldApplier<T> {
	private final Collection<? extends LuceneFieldSpec<T>> typedFields;

	public void addFields(T object, Document doc) {
		typedFields.forEach(fieldSpec -> addField(fieldSpec, object, doc));
	}

	private static <T> void addField(LuceneFieldSpec<T> fieldSpec, T tObject, Document doc) {
		if (tObject == null) {
			return;
		}
		Object value = fieldSpec.toIndexableValue(tObject);
		if (value == null) {
			return;
		}
		if (value instanceof Collection<?> col) {
			col.forEach(e -> doAddField(fieldSpec, e, doc));
		} else {
			doAddField(fieldSpec, value, doc);
		}
	}

	private static void doAddField(LuceneFieldSpec<?> fieldSpec, Object value, Document doc) {
		doc.add(createField(fieldSpec, value));
		addIf(fieldSpec.isPersistent(), () -> createStoredNumberField(fieldSpec, value), doc);
		addIf(fieldSpec.isSortable(), () -> createSortedField(fieldSpec, value), doc);
	}

	private static Field createField(LuceneFieldSpec<?> fieldSpec, Object value) {
		boolean stored = fieldSpec.isPersistent();
		String fieldName = fieldSpec.name();
		return switch (fieldSpec.fieldType()) {
			case KEYWORD, KEYWORD_ARRAY -> keywordField(stored, fieldName, (String) value);
			case WORD -> wordField(stored, fieldName, (String) value);
			case TEXT -> textField(stored, fieldName, (String) value);
			case INT -> intField(fieldName, (Integer) value);
			case LONG -> longField(fieldName, (Long) value);
			case STORED -> storedField(fieldName, (String) value);
		};
	}

	/**
	 * The field must support sorting for this method to be invoked!
	 */
	private static Field createSortedField(LuceneFieldSpec<?> typedField, Object value) {
		return switch (typedField.fieldType()) {
			case KEYWORD, WORD, STORED -> new SortedDocValuesField(
				typedField.name(), new BytesRef((String) value));
			case INT -> new NumericDocValuesField(typedField.name(), (Integer) value);
			case LONG -> new NumericDocValuesField(typedField.name(), (Long) value);
			default -> null;
		};
	}

	/**
	 * IntPoint and LongPoint can't be stored, so we need to add a
	 * separate StoredField instance if we want to also store them.
	 * <p>
	 * The field must be persistent for this method to be invoked!
	 */
	private static Field createStoredNumberField(LuceneFieldSpec<?> typedField, Object value) {
		return switch (typedField.fieldType()) {
			case INT -> new StoredField(typedField.name(), (Integer) value);
			case LONG -> new StoredField(typedField.name(), (Long) value);
			default -> null;
		};
	}

	private static void addIf(boolean condition, Supplier<Field> fieldSupplier, Document doc) {
		if (condition) {
			Field field = fieldSupplier.get();
			if (field != null) {
				doc.add(field);
			}
		}
	}
}
