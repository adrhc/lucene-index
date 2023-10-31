package ro.go.adrhc.persistence.lucene.index.domain.field;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.util.BytesRef;

@RequiredArgsConstructor
public class FieldFactory {
	private final StringFieldFactory stringFieldFactory;

	public static FieldFactory create(Analyzer analyzer) {
		return new FieldFactory(StringFieldFactory.create(analyzer));
	}

	public static IntPoint intField(Enum<?> field, Integer value) {
		return new IntPoint(field.name(), value);
	}

	public static LongField longField(Enum<?> field, Long value) {
		return new LongField(field.name(), value, Field.Store.NO);
	}

	/**
	 * A field that is indexed and tokenized, without term vectors. For example this would be used on a
	 * 'body' field, that contains the bulk of a document's text.
	 */
	public static TextField textField(Enum<?> field, Object value) {
		return new TextField(field.name(), value.toString(), Field.Store.NO);
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
	public static KeywordField keywordField(Enum<?> field, Object value) {
		return new KeywordField(field.name(), value.toString(), Field.Store.NO);
	}

	public static TextField storedTextField(Enum<?> field, Object value) {
		return new TextField(field.name(), value.toString(), Field.Store.YES);
	}

	public static KeywordField storedKeywordField(Enum<?> field, Object value) {
		return new KeywordField(field.name(), value.toString(), Field.Store.YES);
	}

	public static StoredField storedField(Enum<?> field, Object value) {
		return storedField(field.name(), value);
	}

	public static StoredField storedField(String fieldName, Object value) {
		return new StoredField(fieldName, value.toString());
	}

	public Field create(FieldType fieldType, Enum<?> field, Object value) {
		return switch (fieldType) {
			case KEYWORD -> FieldFactory.keywordField(field, value);
			case LONG -> FieldFactory.longField(field, (Long) value);
			case INT -> FieldFactory.intField(field, (Integer) value);
			case PHRASE -> FieldFactory.textField(field, value);
			case STORED -> FieldFactory.storedField(field, value);
			case WORD -> stringFieldFactory.stringField(field, value);
		};
	}
}
