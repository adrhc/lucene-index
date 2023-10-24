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
				" IMG-20210725-WA0029 AaA aAa .bBb ccc_ddd aAșț ttt.ttt x uuu.jpg vvv.jpg .jpeg ");
		assertThat(tokens).containsOnly("img", "20210725", "wa0029",
				"aaa", "bbb", "ccc", "ddd", "aast", "ttt.ttt", "uuu", "vvv");

		tokens = TOKENIZATION_UTILS.tokenize(".jpg");
		assertThat(tokens).isEmpty();

		tokens = TOKENIZATION_UTILS.tokenize(".jpeg");
		assertThat(tokens).isEmpty();

		tokens = TOKENIZATION_UTILS.tokenize(".jpg ");
		assertThat(tokens).isEmpty();

		tokens = TOKENIZATION_UTILS.tokenize(" .jpeg");
		assertThat(tokens).isEmpty();

		tokens = TOKENIZATION_UTILS.tokenize(". jpeg");
		assertThat(tokens).containsOnly("jpeg");
	}
}