package ro.go.adrhc.persistence.lucene.typedindex.factories;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.lucene.analysis.Analyzer;
import ro.go.adrhc.persistence.lucene.core.token.props.TokenizerProperties;

@UtilityClass
public class AnalyzerFactory {
	public static final int NUM_HITS = 10;

	@SneakyThrows
	public static Analyzer defaultAnalyzer() {
		return new ro.go.adrhc.persistence.lucene.core.analysis.AnalyzerFactory(
				new TokenizerProperties()).create();
	}

	@SneakyThrows
	public static Analyzer defaultAnalyzer(TokenizerProperties properties) {
		return new ro.go.adrhc.persistence.lucene.core.analysis.AnalyzerFactory(properties).create();
	}
}
