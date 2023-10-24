package ro.go.adrhc.persistence.lucene.index.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizationUtils;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TokenizationUtilsTest {
	private static final TokenizationUtils TOKENIZATION_UTILS = TokenizationUtilsTestFactory.create();

	@Test
	void tokenize() throws IOException {
		Set<String> tokens = TOKENIZATION_UTILS.tokenize(
				" IMG-20210725-WA0029 AaA x bBb aAa aAșț ttt.ttt ccc_ccc");
		assertThat(tokens).contains("img", "20210725", "wa0029", "aaa", "bbb", "ccc", "aast", "ttt.ttt");
	}
}