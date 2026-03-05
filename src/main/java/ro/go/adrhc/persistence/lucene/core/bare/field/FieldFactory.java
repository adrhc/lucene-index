package ro.go.adrhc.persistence.lucene.core.bare.field;

import lombok.experimental.UtilityClass;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DocValuesType;
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
	public static TextField phraseField(boolean stored, Enum<?> field, String value) {
		return phraseField(stored, field.name(), value);
	}

	public static TextField phraseField(boolean stored, String fieldName, String value) {
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

	/**
	 * Field that indexes a per-document String or {@link BytesRef} into an inverted index for fast
	 * filtering, stores values in a columnar fashion using {@link DocValuesType#SORTED_SET} doc values
	 * for sorting and faceting, and optionally stores values as stored fields for top-hits retrieval.
	 * This field does not support scoring: queries produce constant scores. If you need more
	 * fine-grained control you can use {@link StringField}, {@link SortedDocValuesField} or {@link
	 * SortedSetDocValuesField}, and {@link StoredField}.
	 *
	 * <p>This field defines static factory methods for creating common query objects:
	 *
	 * <ul>
	 *   <li>{@link KeywordField#newExactQuery} for matching a value.
	 *   <li>{@link KeywordField#newSetQuery} for matching any of the values coming from a set.
	 *   <li>{@link KeywordField#newSortField} for matching a value.
	 * </ul>
	 */
	public static KeywordField keywordField(boolean stored, Enum<?> field, String value) {
		return keywordField(stored, field.name(), value);
	}

	public static KeywordField keywordField(boolean stored, String fieldName, String value) {
		return new KeywordField(fieldName, value, stored ? Field.Store.YES : Field.Store.NO);
	}

	public static StoredField storedField(Enum<?> field, String value) {
		return storedField(field.name(), value);
	}

	public static StoredField storedField(String fieldName, String value) {
		return new StoredField(fieldName, value);
	}

	/**
	 * KEYWORD and KEYWORD_ARRAY don't need this because they are sorted by default!
	 */
	public static Field sortField(LuceneFieldSpec<?> typedField, Object value) {
		return switch (typedField.fieldType()) {
			case WORD, STORED -> new SortedDocValuesField(
				typedField.name(), new BytesRef((String) value));
			case INT -> new NumericDocValuesField(typedField.name(), (Integer) value);
			case LONG -> new NumericDocValuesField(typedField.name(), (Long) value);
			default -> null;
		};
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

	public static Field create(LuceneFieldSpec<?> typedField, Object indexableValue) {
		boolean stored = typedField.mustStore();
		String fieldName = typedField.name();
		return switch (typedField.fieldType()) {
			// sorted by default as a multi-values field
			case KEYWORD, KEYWORD_ARRAY -> keywordField(stored, fieldName, (String) indexableValue);
			case WORD -> wordField(stored, fieldName, (String) indexableValue);
			case PHRASE -> phraseField(stored, fieldName, (String) indexableValue);
			case INT -> intField(fieldName, (Integer) indexableValue);
			case LONG -> longField(fieldName, (Long) indexableValue);
			case STORED -> storedField(fieldName, (String) indexableValue);
		};
	}
}
