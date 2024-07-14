package ro.go.adrhc.persistence.lucene.core.bare.analysis;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.miscellaneous.LengthFilterFactory;
import org.apache.lucene.analysis.miscellaneous.RemoveDuplicatesTokenFilterFactory;
import org.apache.lucene.analysis.miscellaneous.TrimFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;

import java.io.IOException;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static org.apache.lucene.analysis.miscellaneous.LengthFilterFactory.MAX_KEY;
import static org.apache.lucene.analysis.miscellaneous.LengthFilterFactory.MIN_KEY;
import static org.apache.lucene.analysis.standard.StandardTokenizer.MAX_TOKEN_LENGTH_LIMIT;

@RequiredArgsConstructor
public class AnalyzerFactory {
	private final TokenizerProperties properties;

	public static Analyzer defaultAnalyzer() throws IOException {
		return new AnalyzerFactory(new TokenizerProperties()).create();
	}

	public static Analyzer defaultAnalyzer(
			TokenizerProperties properties) throws IOException {
		return new AnalyzerFactory(properties).create();
	}

	public Analyzer create() throws IOException {
		CustomAnalyzer.Builder builder = CustomAnalyzer.builder()
				// tokenizer
				.withTokenizer(StandardTokenizerFactory.NAME,
						"maxTokenLength", String.valueOf(MAX_TOKEN_LENGTH_LIMIT));

		trimAsciiFoldingLowerLengthLimitDupsRmTokenStream(builder);
		rmCharsRmTextsRmPatternsSwapPatternsCharFilter(builder);

		return builder.build();
	}

	/**
	 * Chained CharFilter(s):
	 * - MappingCharFilter to replace case-sensitive fixed texts (e.g. special characters) with provided text correspondents
	 * - PatternReplaceCharFilter to remove case-insensitive, fixed texts
	 * - PatternReplaceCharFilter to remove by regex patterns
	 * - PatternReplaceCharFilter using regex patterns to replace with the provided replacement
	 */
	private void rmCharsRmTextsRmPatternsSwapPatternsCharFilter(CustomAnalyzer.Builder builder)
			throws IOException {
		builder.addCharFilter(MappingCharFilterFactory.class,
				properties.getCharactersToReplaceBeforeIndexing());

		for (String text : properties.getFixedPatternsNotToIndex()) {
			builder.addCharFilter(PatternReplaceCharFilterFactory.class,
					"pattern", text, "flags", String.valueOf(CASE_INSENSITIVE | Pattern.LITERAL));
		}

		for (String regex : properties.getRegexPatternsNotToIndex()) {
			builder.addCharFilter(PatternReplaceCharFilterFactory.class,
					"pattern", regex, "flags", String.valueOf(CASE_INSENSITIVE));
		}

		PatternsAndReplacement regexPatternsAndReplacement = properties.getRegexPatternsAndReplacement();
		for (String regex : regexPatternsAndReplacement.patterns()) {
			builder.addCharFilter(PatternReplaceCharFilterFactory.class,
					"pattern", regex, "flags", String.valueOf(CASE_INSENSITIVE),
					"replacement", regexPatternsAndReplacement.replacement());
		}
	}

	/**
	 * Chained TokenStream(s):
	 * - TrimFilter
	 * - ASCIIFoldingFilter
	 * - LengthFilter
	 * - LowerCaseFilter
	 * - RemoveDuplicatesTokenFilter
	 */
	private void trimAsciiFoldingLowerLengthLimitDupsRmTokenStream(CustomAnalyzer.Builder builder)
			throws IOException {
		builder
				.addTokenFilter(TrimFilterFactory.NAME)
				.addTokenFilter(ASCIIFoldingFilterFactory.NAME)
				.addTokenFilter(LengthFilterFactory.NAME,
						MIN_KEY, String.valueOf(properties.getMinTokenLength()),
						MAX_KEY, String.valueOf(MAX_TOKEN_LENGTH_LIMIT))
				.addTokenFilter(LowerCaseFilterFactory.NAME)
				.addTokenFilter(RemoveDuplicatesTokenFilterFactory.NAME);
	}
}
