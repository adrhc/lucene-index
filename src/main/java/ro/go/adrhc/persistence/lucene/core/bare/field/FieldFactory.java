package ro.go.adrhc.persistence.lucene.core.bare.field;

import lombok.experimental.UtilityClass;
import org.apache.lucene.document.*;
import org.apache.lucene.util.BytesRef;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

@UtilityClass
public class FieldFactory {
	/**
	 * If you also need to store the value, you should
	 * add a separate {@link StoredField} instance.
	 */
	public static IntPoint intField(Enum<?> field, Integer value) {
		return intField(field.name(), value);
	}

	/**
	 * If you also need to store the value, you should
	 * add a separate {@link StoredField} instance.
	 */
	public static IntPoint intField(String fieldName, Integer value) {
		return new IntPoint(fieldName, value);
	}

	public static LongPoint longField(Enum<?> field, Long value) {
		return longField(field.name(), value);
	}

	public static LongPoint longField(String fieldName, Long value) {
		return new LongPoint(fieldName, value);
	}

	/**
	 * A field that is indexed and tokenized, without term vectors. For example this
	 * would be used on a 'body' field, that contains the bulk of a document's text.
	 */
	public static TextField textField(boolean stored, Enum<?> field, String value) {
		return textField(stored, field.name(), value);
	}

	public static TextField textField(boolean stored, String fieldName, String value) {
		return new TextField(fieldName, value, stored ? Field.Store.YES : Field.Store.NO);
	}

	/**
	 * The field is not tokenized, but only normalized (i.e., char-filtered) before indexing!
	 */
	public static TextField wordField(boolean stored, Enum<?> field, String value) {
		return wordField(stored, field.name(), value);
	}

	public static TextField wordField(boolean stored, String fieldName, String value) {
		return new TextField(fieldName, value, stored ? Field.Store.YES : Field.Store.NO);
	}

	public static StringField keywordField(boolean stored, String fieldName, String value) {
		return new StringField(fieldName, value, stored ? Field.Store.YES : Field.Store.NO);
	}

	public static StoredField storedField(Enum<?> field, String value) {
		return storedField(field.name(), value);
	}

	public static StoredField storedField(String fieldName, String value) {
		return new StoredField(fieldName, value);
	}

	/**
	 * IntPoint and LongPoint can't be stored, so we need to add a
	 * separate StoredField instance if we want to also store them.
	 */
	public static Field storedNumber(LuceneFieldSpec<?> typedField, Object value) {
		return switch (typedField.fieldType()) {
			case INT -> new StoredField(typedField.name(), (Integer) value);
			case LONG -> new StoredField(typedField.name(), (Long) value);
			default -> null;
		};
	}

	/**
	 * The field must support sorting for this method to be invoked!
	 */
	public static Field sortField(LuceneFieldSpec<?> typedField, Object value) {
		return switch (typedField.fieldType()) {
			case KEYWORD, WORD, STORED -> new SortedDocValuesField(
				typedField.name(), new BytesRef((String) value));
			case INT -> new NumericDocValuesField(typedField.name(), (Integer) value);
			case LONG -> new NumericDocValuesField(typedField.name(), (Long) value);
			default -> null;
		};
	}

	public static Field create(LuceneFieldSpec<?> typedField, Object indexableValue) {
		boolean stored = typedField.isPersistent();
		String fieldName = typedField.name();
		return switch (typedField.fieldType()) {
			case KEYWORD, KEYWORD_ARRAY -> keywordField(stored, fieldName, (String) indexableValue);
			case WORD -> wordField(stored, fieldName, (String) indexableValue);
			case TEXT -> textField(stored, fieldName, (String) indexableValue);
			case INT -> intField(fieldName, (Integer) indexableValue);
			case LONG -> longField(fieldName, (Long) indexableValue);
			case STORED -> storedField(fieldName, (String) indexableValue);
		};
	}
}
