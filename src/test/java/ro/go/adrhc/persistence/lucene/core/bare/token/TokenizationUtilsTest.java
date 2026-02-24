package ro.go.adrhc.persistence.lucene.core.bare.token;

import org.junit.jupiter.api.Test;
import ro.go.adrhc.persistence.lucene.core.bare.analysis.TokenizerProperties;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.rainerhahnekamp.sneakythrow.Sneaky.sneaked;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ro.go.adrhc.persistence.lucene.core.bare.analysis.AnalyzerFactory.defaultAnalyzer;
import static ro.go.adrhc.persistence.lucene.core.bare.analysis.PatternsAndReplacement.caseInsensitive;

public class TokenizationUtilsTest {
	public static final String TEXT = " IMG-20210725-WA0029 AaA aAa .bBb ccc_ddd ccc-ddd 555-888 " +
	                                  "ăĂîÎșȘțȚ ttt.ttt x uuu.jPg vvv.jpg .jpEg \"fixed Pattern TO Remove\" (Regex Pattern TO Remove) ";
	private static final TokenizationUtils CUSTOM_TOKENIZER =
		new TokenizationUtils(defaultAnalyzer(createTokenizerProperties()).orElseThrow());

	@Test
	void testTokenizerPropertiesImpact() throws IOException {
		Set<String> tokens = CUSTOM_TOKENIZER.textToTokenSet(TEXT);
		assertThat(tokens).containsOnly("img", "20210725", "wa0029",
			"aaa", "bbb", "ccc", "ddd", "555", "888", "aaiisstt", "ttt.ttt", "uuu", "vvv");

		assertTrue(Stream.of(".jPg", ".jpEg", ".jPg ", " .jpEg", " .jpEg ", " .jPg ")
			.map(sneaked(CUSTOM_TOKENIZER::textToTokenSet))
			.allMatch(Collection::isEmpty));

		assertThat(Stream.of(". jPg", ". jpEg")
			.map(sneaked(CUSTOM_TOKENIZER::textToTokenSet))
			.flatMap(Collection::stream))
			.containsOnly("jpg", "jpeg");

		assertThat(CUSTOM_TOKENIZER.wordsToTokenSet(Set.of(".jPg123", ".jpEg123")))
			.containsOnly("123");
	}

	private static TokenizerProperties createTokenizerProperties() {
		return new TokenizerProperties(2,
			List.of("Fixed Pattern To Remove"),
			List.of("\\(\\s*Regex\\s*Pattern\\s*To\\s*Remove\\)"),
			Map.of("_", " "),
			caseInsensitive("$1", "([^\\s]*)\\.jpe?g"));
	}
}