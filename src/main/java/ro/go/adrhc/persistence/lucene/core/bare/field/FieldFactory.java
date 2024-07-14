package ro.go.adrhc.persistence.lucene.core.bare.field;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.util.BytesRef;
import ro.go.adrhc.util.Assert;

import static ro.go.adrhc.persistence.lucene.core.bare.field.FieldType.INT;
import static ro.go.adrhc.persistence.lucene.core.bare.field.FieldType.STORED;

@RequiredArgsConstructor
public class FieldFactory {
	private final WordFieldFactory wordFieldFactory;

	public static FieldFactory create(Analyzer analyzer) {
		return new FieldFactory(WordFieldFactory.of(analyzer));
	}

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

	public static LongField longField(boolean stored, Enum<?> field, Long value) {
		return longField(stored, field.name(), value);
	}

	public static LongField longField(boolean stored, String fieldName, Long value) {
		return new LongField(fieldName, value, stored ? Field.Store.YES : Field.Store.NO);
	}

	/**
	 * A field that is indexed and tokenized, without term vectors. For example this
	 * would be used on a 'body' field, that contains the bulk of a document's text.
	 */
	public static TextField phraseField(boolean stored, Enum<?> field, Object value) {
		return phraseField(stored, field.name(), value.toString());
	}

	public static TextField phraseField(boolean stored, String fieldName, Object value) {
		return new TextField(fieldName, value.toString(),
				stored ? Field.Store.YES : Field.Store.NO);
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
	public static KeywordField keywordField(boolean stored, Enum<?> field, Object value) {
		return keywordField(stored, field.name(), value.toString());
	}

	public static KeywordField keywordField(boolean stored, String fieldName, Object value) {
		return new KeywordField(fieldName, value.toString(),
				stored ? Field.Store.YES : Field.Store.NO);
	}

	public static StoredField storedField(Enum<?> field, Object value) {
		return storedField(field.name(), value);
	}

	public static StoredField storedField(String fieldName, Object value) {
		return new StoredField(fieldName, value.toString());
	}

	public Field create(boolean stored, FieldType fieldType, Enum<?> field, Object value) {
		return create(stored, fieldType, field.name(), value);
	}

	public Field create(boolean stored, FieldType fieldType, String fieldName, Object value) {
		Assert.isTrue(fieldType != STORED || stored,
				"STORED fields must demand to be stored!");
		Assert.isTrue(fieldType != INT || !stored,
				"INT fields must not demand to be stored!");
		return switch (fieldType) {
			case KEYWORD -> keywordField(stored, fieldName, value);
			case WORD -> wordFieldFactory.wordField(stored, fieldName, value);
			case PHRASE, TAGS -> phraseField(stored, fieldName, value);
			case INT -> intField(fieldName, (Integer) value);
			case LONG -> longField(stored, fieldName, (Long) value);
			case STORED -> storedField(fieldName, value);
		};
	}
}
