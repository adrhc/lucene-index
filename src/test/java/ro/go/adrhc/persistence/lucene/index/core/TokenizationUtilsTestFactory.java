package ro.go.adrhc.persistence.lucene.index.core;

import lombok.experimental.UtilityClass;
import ro.go.adrhc.persistence.lucene.index.core.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizationUtils;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties;

import java.util.List;

@UtilityClass
public class TokenizationUtilsTestFactory {
	public static TokenizationUtils create() {
		return create(2);
	}

	public static TokenizationUtils create(int minTokenLength) {
		AnalyzerFactory analyzerFactory =
				new AnalyzerFactory(TokenizerProperties.of(minTokenLength,
						List.of(new String[]{"_", " "}, new String[]{"-", " "})));
		return new TokenizationUtils(analyzerFactory);
	}
}
