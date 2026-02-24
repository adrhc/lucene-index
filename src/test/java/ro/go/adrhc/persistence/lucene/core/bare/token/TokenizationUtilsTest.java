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
import static java.lang.String.join;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ro.go.adrhc.persistence.lucene.core.bare.analysis.AnalyzerFactory.defaultAnalyzer;
import static ro.go.adrhc.persistence.lucene.core.bare.analysis.PatternsAndReplacement.caseInsensitive;
import static ro.go.adrhc.persistence.lucene.core.bare.token.AudioTokenizationTest.assertAudioTokens;
import static ro.go.adrhc.persistence.lucene.core.bare.token.TestData.TEXT;

class TokenizationUtilsTest {
	public static final TokenizationUtils SIMPLE_TOKENIZER =
		new TokenizationUtils(defaultAnalyzer(new TokenizerProperties()).orElseThrow());
	private static final TokenizationUtils CUSTOM_TOKENIZER =
		new TokenizationUtils(defaultAnalyzer(createTokenizerProperties()).orElseThrow());
	private static final char[] SPECIAL_CHARACTERS = {
		'!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
		':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~'
	};
	private static final Map<Character, CharReplacements> REPLACEMENTS = Map.of(
		'\'', new CharReplacements("'", "'"),
		'_', new CharReplacements("_", " "),
		'.', new CharReplacements(".", " "),
		':', new CharReplacements(":", " ")
	);

	@Test
	void tokenizerRegexPatternsToReplaceComparison() throws IOException {
		tokenizerComparison("filename.mp3", "filename.mp3", "filename");
		tokenizerComparison("greengirl_15_williamson.mp3",
			"greengirl_15_williamson.mp3", "15 greengirl williamson");
	}

	@Test
	void tokenizerCharToSpaceReplacementComparison() throws IOException {
		CharReplacements defaultReplacement = new CharReplacements(" ", " ");
		for (char specialCharacter : SPECIAL_CHARACTERS) {
			String text = "LEFT" + specialCharacter + "RIGHT";
			String simple = REPLACEMENTS.getOrDefault(specialCharacter, defaultReplacement).simple;
			String audio = REPLACEMENTS.getOrDefault(specialCharacter, defaultReplacement).audio;
			tokenizerComparison(text, "left" + simple + "right", "left" + audio + "right");
		}
	}

	@Test
	void customTokenizerTest() throws IOException {
		Set<String> tokens = CUSTOM_TOKENIZER.textToTokenSet(TEXT);
		assertThat(tokens).containsOnly("img", "20210725", "wa0029",
			"aaa", "bbb", "ccc", "ddd", "555", "888", "aaiisstt", "ttt", "uuu", "vvv");

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

	private static void tokenizerComparison(String text,
		String simpleTokens, String audioTokens) throws IOException {
		assertSimpleTokens(text, simpleTokens);
		assertAudioTokens(text, audioTokens);
	}

	private static void assertSimpleTokens(String text, String expectedTokens) throws IOException {
		assertEquals(expectedTokens, join(" ", SIMPLE_TOKENIZER.textToTokenList(text)));
	}

	private static TokenizerProperties createTokenizerProperties() {
		return new TokenizerProperties(2,
			List.of("Fixed Pattern To Remove"),
			List.of("\\(\\s*Regex\\s*Pattern\\s*To\\s*Remove\\)"),
			caseInsensitive("$1", "([^\\s]*)\\.jpe?g"),
			Map.of("_", " ", ".", " ", ":", " ")
		);
	}

	private record CharReplacements(String simple, String audio) {
	}
}