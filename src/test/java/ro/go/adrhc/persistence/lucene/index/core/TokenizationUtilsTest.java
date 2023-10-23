package ro.go.adrhc.persistence.lucene.index.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.index.core.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TokenizationUtilsTest {
	private static final AnalyzerFactory ANALYZER_FACTORY =
			new AnalyzerFactory(TokenizerProperties.of(2,
					List.of(new String[]{"_", " "}, new String[]{"-", " "})));
	private static final TokenizationUtils TOKENIZATION_UTILS =
			new TokenizationUtils(ANALYZER_FACTORY);

	@Test
	void tokenize() throws IOException {
		Set<String> tokens = TOKENIZATION_UTILS.tokenize("AaA x bBb aAa");
		assertThat(tokens).containsExactly("aaa", "bbb");
	}

	@Test
	void tokenizeFilename() throws IOException {
		Set<String> tokens = TOKENIZATION_UTILS.tokenize("IMG-20210725-WA0029");
		assertThat(tokens).containsExactly("img", "20210725", "wa0029");
	}
}