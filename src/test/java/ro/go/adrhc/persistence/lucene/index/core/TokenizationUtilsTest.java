package ro.go.adrhc.persistence.lucene.index.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.index.IndexTestFactories;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizationUtils;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TokenizationUtilsTest {
	private static final TokenizationUtils TOKENIZATION_UTILS = IndexTestFactories.createTokenizationUtils();

	@Test
	void tokenize() throws IOException {
		Set<String> tokens = TOKENIZATION_UTILS.tokenize(
				" IMG-20210725-WA0029 AaA aAa .bBb ccc_ddd ccc-ddd " +
						"555-888 aAșț ttt.ttt x uuu.jPg vvv.jpg .jpEg " +
						"\"fixed Pattern TO Remove\" (Regex Pattern TO Remove)");
		assertThat(tokens).containsOnly("img", "20210725", "wa0029",
				"aaa", "bbb", "ccc", "ddd", "555", "888", "aast", "ttt.ttt", "uuu", "vvv");

		tokens = TOKENIZATION_UTILS.tokenize(".jPg");
		assertThat(tokens).isEmpty();

		tokens = TOKENIZATION_UTILS.tokenize(".jpEg");
		assertThat(tokens).isEmpty();

		tokens = TOKENIZATION_UTILS.tokenize(".jPg ");
		assertThat(tokens).isEmpty();

		tokens = TOKENIZATION_UTILS.tokenize(" .jpEg");
		assertThat(tokens).isEmpty();

		tokens = TOKENIZATION_UTILS.tokenize(". jpEg");
		assertThat(tokens).containsOnly("jpeg");
	}
}