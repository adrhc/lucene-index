package ro.go.adrhc.persistence.lucene.core.bare.analysis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.miscellaneous.LengthFilterFactory;
import org.apache.lucene.analysis.miscellaneous.RemoveDuplicatesTokenFilterFactory;
import org.apache.lucene.analysis.miscellaneous.TrimFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;

import java.io.IOException;
import java.util.Optional;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.LITERAL;
import static org.apache.lucene.analysis.miscellaneous.LengthFilterFactory.MAX_KEY;
import static org.apache.lucene.analysis.miscellaneous.LengthFilterFactory.MIN_KEY;
import static org.apache.lucene.analysis.standard.StandardTokenizer.MAX_TOKEN_LENGTH_LIMIT;
import static ro.go.adrhc.util.optional.OptionalFactory.ofSilencedRiskySupplier;

@RequiredArgsConstructor
@Slf4j
public class AnalyzerFactory {
	private final TokenizerProperties properties;

	public static Analyzer defaultAnalyzer() throws IOException {
		return defaultAnalyzer(new TokenizerProperties())
			.orElseThrow(() -> new IOException("Failed to create the default analyzer!"));
	}

	public static Optional<Analyzer> defaultAnalyzer(TokenizerProperties properties) {
		return new AnalyzerFactory(properties).create();
	}

	public Optional<Analyzer> create() {
		return ofSilencedRiskySupplier(() -> {
			CustomAnalyzer.Builder builder = withMaxTokenLength();
			addTrimAsciiFoldingLengthLowerRmDupsTokenFilters(builder);
			addCharReplacerRmTextsAndPatternsPatternReplacerCharFilters(builder);
			return builder.build();
		});
	}

	private CustomAnalyzer.Builder withMaxTokenLength() throws IOException {
		return CustomAnalyzer.builder()
			.withTokenizer(StandardTokenizerFactory.NAME,
				"maxTokenLength", String.valueOf(MAX_TOKEN_LENGTH_LIMIT));
	}

	/**
	 * Chained TokenStream(s):
	 * - TrimFilter
	 * - ASCIIFoldingFilter
	 * - LengthFilter
	 * - LowerCaseFilter
	 * - RemoveDuplicatesTokenFilter
	 */
	private void addTrimAsciiFoldingLengthLowerRmDupsTokenFilters(
		CustomAnalyzer.Builder builder) throws IOException {
		builder
			.addTokenFilter(TrimFilterFactory.NAME)
			.addTokenFilter(ASCIIFoldingFilterFactory.NAME)
			.addTokenFilter(LengthFilterFactory.NAME,
				MIN_KEY, String.valueOf(properties.getMinTokenLength()),
				MAX_KEY, String.valueOf(MAX_TOKEN_LENGTH_LIMIT))
			.addTokenFilter(LowerCaseFilterFactory.NAME)
			.addTokenFilter(RemoveDuplicatesTokenFilterFactory.NAME);
	}

	/**
	 * Chained CharFilter(s):
	 * - MappingCharFilter to replace case-sensitive fixed texts (e.g. special characters) with provided text correspondents
	 * - PatternReplaceCharFilter to remove case-insensitive, fixed texts
	 * - PatternReplaceCharFilter to remove by regex patterns
	 * - PatternReplaceCharFilter using regex patterns to replace with the provided replacement
	 */
	private void addCharReplacerRmTextsAndPatternsPatternReplacerCharFilters(
		CustomAnalyzer.Builder builder) throws IOException {
		for (String text : properties.getFixedPatternsNotToIndex()) {
			builder.addCharFilter(PatternReplaceCharFilterFactory.class,
				"pattern", text, "flags", String.valueOf(CASE_INSENSITIVE | LITERAL));
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

		builder.addCharFilter(MappingCharFilterFactory.class,
			properties.getCharactersToReplaceBeforeIndexing());
	}
}
