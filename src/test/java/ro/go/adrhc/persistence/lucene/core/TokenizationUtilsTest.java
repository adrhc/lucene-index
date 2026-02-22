package ro.go.adrhc.persistence.lucene.core;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.core.bare.analysis.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.core.bare.token.TokenizationUtils;

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

@ExtendWith(MockitoExtension.class)
@Slf4j
public class TokenizationUtilsTest {
	public static final String TEXT = " IMG-20210725-WA0029 AaA aAa .bBb ccc_ddd ccc-ddd 555-888 " +
	                                  "ăĂîÎșȘțȚ ttt.ttt x uuu.jPg vvv.jpg .jpEg \"fixed Pattern TO Remove\" (Regex Pattern TO Remove) ";
	private static final TokenizationUtils TOKENIZATION_UTILS =
		new TokenizationUtils(defaultAnalyzer(createTokenizerProperties()).orElseThrow());
	private static final TokenizationUtils AUDIO_TOKENIZER =
		new TokenizationUtils(defaultAnalyzer(audioTokenizerProperties()).orElseThrow());

	@Test
	void test() throws IOException {
		Set<String> tokens = TOKENIZATION_UTILS.textToTokenSet(TEXT);
		assertThat(tokens).containsOnly("img", "20210725", "wa0029",
			"aaa", "bbb", "ccc", "ddd", "555", "888", "aaiisstt", "ttt.ttt", "uuu", "vvv");

		assertTrue(Stream.of(".jPg", ".jpEg", ".jPg ", " .jpEg", " .jpEg ", " .jPg ")
			.map(sneaked(TOKENIZATION_UTILS::textToTokenSet))
			.allMatch(Collection::isEmpty));

		assertThat(Stream.of(". jPg", ". jpEg")
			.map(sneaked(TOKENIZATION_UTILS::textToTokenSet))
			.flatMap(Collection::stream))
			.containsOnly("jpg", "jpeg");

		assertThat(TOKENIZATION_UTILS.wordsToTokenSet(Set.of(".jPg123", ".jpEg123")))
			.containsOnly("123");
	}

	@Test
	void audioTokenizerTest() throws IOException {
		assertTokens("Smiley - Vals (Official).mp3", "smiley vals");
		assertTokens("Smiley - Vals (Official Video) Version.mp3", "smiley vals version");
		assertTokens("Smiley - Vals (Official Video Version.mp3", "smiley vals");
		assertTokens("Smiley - Vals Official Video) Version.mp3", "smiley vals version");
		assertTokens("Smiley - Vals Official Video Version.mp3", "smiley vals");
		assertTokens("Yazoo - Don'$'\\'''t Go (Official HD Video) - Yaz.mp3", "yazoo don go yaz");
		assertTokens("test (Audio Version) title.mp3", "test title");
		assertTokens("The Wallflowers - One Headlight (Official Music Video).mp3", "the wallflowers one headlight");
		assertTokens("Dr Alban - Hello Africa (Official HD).mp3", "dr alban hello africa");
	}

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
			// character to replace in the text before tokenization
			Map.of("_", " "),
			// regex patterns to replace in the text before tokenization
			caseInsensitive("$1", "([^\\s]*)\\.mp3")
		);
	}

	private static TokenizerProperties createTokenizerProperties() {
		return new TokenizerProperties(2,
			List.of("Fixed Pattern To Remove"),
			List.of("\\(\\s*Regex\\s*Pattern\\s*To\\s*Remove\\)"),
			Map.of("_", " "),
			caseInsensitive("$1", "([^\\s]*)\\.jpe?g"));
	}

	private void assertTokens(String text, String expectedSpaceJoinedTokens) throws IOException {
		String actual = join(" ", AUDIO_TOKENIZER.textToTokenList(text));
		assertEquals(expectedSpaceJoinedTokens, actual);
	}
}