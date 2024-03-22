package ro.go.adrhc.persistence.lucene.core.field;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import ro.go.adrhc.persistence.lucene.core.token.TokenizationUtils;

@RequiredArgsConstructor
public class WordFieldFactory {
    private final TokenizationUtils tokenizationUtils;

    public static WordFieldFactory create(Analyzer analyzer) {
        return new WordFieldFactory(new TokenizationUtils(analyzer));
    }

    /**
     * A field that is indexed but not tokenized: the entire String value is indexed as a single token.
     * For example this might be used for a 'country' field or an 'id' field. If you also need to sort
     * on this field, separately add a {@link SortedDocValuesField} to your document.
     * <p>
     * The field is still normalized (aka, filtered) before indexing!
     */
    public StringField wordField(boolean stored, Enum<?> field, Object value) {
        return wordField(stored, field.name(), value);
    }

    public StringField wordField(boolean stored, String fieldName, Object value) {
        return new StringField(fieldName,
                tokenizationUtils.normalize(fieldName, value.toString()),
                stored ? Field.Store.YES : Field.Store.NO);
    }
}
