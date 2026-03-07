package ro.go.adrhc.persistence.lucene.core.bare.field;

import lombok.experimental.UtilityClass;
import org.apache.lucene.document.*;

@UtilityClass
public class FieldFactory {
	/**
	 * If you also need to store the value, you should
	 * add a separate {@link StoredField} instance.
	 */
	public static IntPoint intField(String fieldName, Integer value) {
		return new IntPoint(fieldName, value);
	}

	/**
	 * If you also need to store the value, you should
	 * add a separate {@link StoredField} instance.
	 */
	public static LongPoint longField(String fieldName, Long value) {
		return new LongPoint(fieldName, value);
	}

	/**
	 * A field that is indexed and tokenized, without term vectors. For example this
	 * would be used on a 'body' field, that contains the bulk of a document's text.
	 */
	public static TextField textField(boolean stored, String fieldName, String value) {
		return new TextField(fieldName, value, stored ? Field.Store.YES : Field.Store.NO);
	}

	/**
	 * The field is not tokenized, but only normalized (i.e., char-filtered) before indexing!
	 */
	public static TextField wordField(boolean stored, String fieldName, String value) {
		return new TextField(fieldName, value, stored ? Field.Store.YES : Field.Store.NO);
	}

	public static StringField keywordField(boolean stored, String fieldName, String value) {
		return new StringField(fieldName, value, stored ? Field.Store.YES : Field.Store.NO);
	}

	public static StoredField storedField(String fieldName, String value) {
		return new StoredField(fieldName, value);
	}
}
