package ro.go.adrhc.persistence.lucene.index.domain.field;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import ro.go.adrhc.persistence.lucene.core.tokenizer.TokenizationUtils;

@RequiredArgsConstructor
public class StringFieldFactory {
	private final TokenizationUtils tokenizationUtils;

	public static StringFieldFactory create(Analyzer analyzer) {
		return new StringFieldFactory(new TokenizationUtils(analyzer));
	}

	/**
	 * A field that is indexed but not tokenized: the entire String value is indexed as a single token.
	 * For example this might be used for a 'country' field or an 'id' field. If you also need to sort
	 * on this field, separately add a {@link SortedDocValuesField} to your document.
	 */
	public StringField stringField(boolean stored, Enum<?> field, Object value) {
		return stringField(stored, field.name(), value);
	}

	public StringField stringField(boolean stored, String fieldName, Object value) {
		return new StringField(fieldName,
				tokenizationUtils.normalize(fieldName, value.toString()),
				stored ? Field.Store.YES : Field.Store.NO);
	}
}
