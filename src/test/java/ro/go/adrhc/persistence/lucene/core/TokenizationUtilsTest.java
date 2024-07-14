package ro.go.adrhc.persistence.lucene.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import static com.rainerhahnekamp.sneakythrow.Sneaky.sneaked;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ro.go.adrhc.persistence.lucene.TypedIndexParamsTestFactory.TOKENIZATION_UTILS;

@ExtendWith(MockitoExtension.class)
public
class TokenizationUtilsTest {
	public static final String TEXT = " IMG-20210725-WA0029 AaA aAa .bBb ccc_ddd ccc-ddd 555-888 " +
			"ăĂîÎșȘțȚ ttt.ttt x uuu.jPg vvv.jpg .jpEg \"fixed Pattern TO Remove\" (Regex Pattern TO Remove) ";

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
}