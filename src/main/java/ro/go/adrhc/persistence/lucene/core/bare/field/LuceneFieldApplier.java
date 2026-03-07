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
		Object fValue = fieldSpec.toIndexableValue(tObject);
		if (fValue == null) {
			return;
		}
		if (fValue instanceof Collection<?> col) {
			col.forEach(elem -> addOneField(fieldSpec, elem, doc));
		} else {
			addOneField(fieldSpec, fValue, doc);
		}
	}

	private static void addOneField(LuceneFieldSpec<?> fieldSpec, Object fValue, Document doc) {
		doc.add(createField(fieldSpec, fValue));
		addIf(fieldSpec.isPersistent(), () -> createStoredNumberField(fieldSpec, fValue), doc);
		addIf(fieldSpec.isSortable(), () -> createSortedField(fieldSpec, fValue), doc);
	}

	private static Field createField(LuceneFieldSpec<?> fieldSpec, Object fValue) {
		boolean stored = fieldSpec.isPersistent();
		String fieldName = fieldSpec.name();
		return switch (fieldSpec.fieldType()) {
			case KEYWORD, KEYWORD_ARRAY -> keywordField(stored, fieldName, (String) fValue);
			case WORD -> wordField(stored, fieldName, (String) fValue);
			case TEXT -> textField(stored, fieldName, (String) fValue);
			case INT -> intField(fieldName, (Integer) fValue);
			case LONG -> longField(fieldName, (Long) fValue);
			case STORED -> storedField(fieldName, (String) fValue);
		};
	}

	/**
	 * The field must support sorting for this method to be invoked!
	 */
	private static Field createSortedField(LuceneFieldSpec<?> typedField, Object fValue) {
		return switch (typedField.fieldType()) {
			case KEYWORD, WORD, STORED -> new SortedDocValuesField(
				typedField.name(), new BytesRef((String) fValue));
			case INT -> new NumericDocValuesField(typedField.name(), (Integer) fValue);
			case LONG -> new NumericDocValuesField(typedField.name(), (Long) fValue);
			default -> null;
		};
	}

	/**
	 * IntPoint and LongPoint can't be stored, so we need to add a
	 * separate StoredField instance if we want to also store them.
	 * <p>
	 * The field must be persistent for this method to be invoked!
	 */
	private static Field createStoredNumberField(LuceneFieldSpec<?> typedField, Object fValue) {
		return switch (typedField.fieldType()) {
			case INT -> new StoredField(typedField.name(), (Integer) fValue);
			case LONG -> new StoredField(typedField.name(), (Long) fValue);
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
