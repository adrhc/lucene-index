package ro.go.adrhc.persistence.lucene.core.bare.token;

import lombok.experimental.UtilityClass;
import ro.go.adrhc.persistence.lucene.core.bare.analysis.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.lib.TokenStreamToStreamConverter;

import java.util.List;
import java.util.Map;

import static ro.go.adrhc.persistence.lucene.core.bare.analysis.AnalyzerFactory.defaultAnalyzer;
import static ro.go.adrhc.persistence.lucene.core.bare.analysis.PatternsAndReplacement.caseInsensitive;

@UtilityClass
public class AudioTokenizationUtils {
	public static final String SPECIAL_WORDS = "(\\d{4,4}|audio|complete|edition|hd|mix|movie|music|of+iciala?|originala?|orchestral|remaster|single|theme|version|versiunea|video|vinyl)";
	public static final TokenizationUtils AUDIO_TOKENIZER =
		new TokenizationUtils(new TokenStreamToStreamConverter(),
			defaultAnalyzer(audioTokenizerProperties()).orElseThrow());

	private static TokenizerProperties audioTokenizerProperties() {
		return new TokenizerProperties(2,
			// fixed patterns not to index
			List.of(),
			// regex patterns not to index
			List.of(
				"\\((\\s*" + SPECIAL_WORDS + "){2,}\\s*\\)", // left & right parentheses
				"\\((\\s*" + SPECIAL_WORDS + "){2,}", // left parentheses only
				"(" + SPECIAL_WORDS + "\\s*){2,}\\)", // right parentheses only
				SPECIAL_WORDS + "(\\s*" + SPECIAL_WORDS + "){2,}", // no parentheses but at least 3 special words in a row
				"\\(\\s*(\\d{4,4}|acoustic|ac+oustique|audio|concert|edit|lyrics?|of+iciala?|originala?|remaster|remix|rmx|rock|soundtrack)\\s*\\)",
				"\\(\\s*(disc|parts?)\\s*\\d+\\s*\\)" // e.g., (disc 1)
			),
			// regex patterns to replace in the text before tokenization
			caseInsensitive("$1", "([^\\s]*)\\.mp3"),
			// character to replace in the text before tokenization
			Map.of("_", " ", ".", " ", ":", " ")
		);
	}
}
