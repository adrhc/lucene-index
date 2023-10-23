package ro.go.adrhc.persistence.lucene.index.core.analysis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.index.core.TokenizationUtils;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AnalyzerFactoryTest {
	private static final AnalyzerFactory ANALYZER_FACTORY =
			new AnalyzerFactory(TokenizerProperties.of(2));
	private static final TokenizationUtils TOKENIZATION_UTILS =
			new TokenizationUtils(ANALYZER_FACTORY);

	@Test
	void create() throws IOException {
		Set<String> tokens = TOKENIZATION_UTILS.tokenize("AaA x bBb aAa");
		assertThat(tokens).containsExactly("aaa", "bbb");
	}
}