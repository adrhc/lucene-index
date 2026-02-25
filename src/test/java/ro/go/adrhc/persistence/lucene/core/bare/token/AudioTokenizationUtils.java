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
	public static final TokenizationUtils AUDIO_TOKENIZER =
		new TokenizationUtils(TokenStreamToStreamConverter.of(),
			defaultAnalyzer(audioTokenizerProperties()).orElseThrow());

	private static TokenizerProperties audioTokenizerProperties() {
		return new TokenizerProperties(2,
			// fixed patterns not to index
			List.of(),
			// regex patterns not to index
			List.of(
				"\\(\\s*(Audio|HD|Of+iciala?|Originala?|Music|Version|Versiunea|Video|Vinyl)(\\s*(Audio|HD|Of+iciala?|Originala?|Music|Version|Versiunea|Video|Vinyl))+\\s*\\)",
				"\\(\\s*(Audio|HD|Of+iciala?|Originala?|Music|Version|Versiunea|Video|Vinyl)(\\s*(Audio|HD|Of+iciala?|Originala?|Music|Version|Versiunea|Video|Vinyl))+",
				"(Audio|HD|Of+iciala?|Originala?|Music|Version|Versiunea|Video|Vinyl)(\\s*(Audio|HD|Of+iciala?|Originala?|Music|Version|Versiunea|Video|Vinyl))+\\s*\\)",
				"(Audio|HD|Of+iciala?|Originala?|Music|Version|Versiunea|Video|Vinyl)(\\s*(Audio|HD|Of+iciala?|Originala?|Music|Version|Versiunea|Video|Vinyl)){2,}",
				"\\(\\s*(Of+iciala?|Originala?)\\s*\\)"
			),
			// regex patterns to replace in the text before tokenization
			caseInsensitive("$1", "([^\\s]*)\\.mp3"),
			// character to replace in the text before tokenization
			Map.of("_", " ", ".", " ", ":", " ")
		);
	}
}
