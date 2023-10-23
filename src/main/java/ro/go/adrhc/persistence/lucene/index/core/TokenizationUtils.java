package ro.go.adrhc.persistence.lucene.index.core;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import ro.go.adrhc.persistence.lucene.index.core.analysis.AnalyzerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class TokenizationUtils {
	private final AnalyzerFactory analyzerFactory;

	public boolean containedDiffersSlightly(int levenshteinDistance,
			Set<String> containerTokens, String contained) throws IOException {
		Set<String> containedTokens = tokenize(contained);
		return containedDiffersSlightly(levenshteinDistance, containerTokens, containedTokens);
	}

	public Set<String> tokenizeAll(@NonNull Collection<String> words) throws IOException {
		Set<String> result = new HashSet<>();
		for (String w : words) {
			result.addAll(tokenize(w));
		}
		return result;
	}

	public Set<String> tokenize(String text) throws IOException {
		try (Analyzer analyzer = analyzerFactory.create()) {
			try (TokenStream tokenStream = analyzer.tokenStream(null, text)) {
				return doTokenize(tokenStream);
			}
		}
	}

	private Set<String> doTokenize(TokenStream tokenStream) throws IOException {
		tokenStream.reset();
		Set<String> tokens = new HashSet<>();
		CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
		while (tokenStream.incrementToken()) {
			tokens.add(termAttribute.toString());
		}
		tokenStream.end();
		return tokens;
	}

	private boolean containedDiffersSlightly(int levenshteinDistance,
			Set<String> containerTokens, Set<String> containedTokens) {
		return SetUtils.difference(containedTokens, containerTokens).stream()
				.allMatch(contained -> containerTokens.stream()
						.anyMatch(container -> LevenshteinDistance.getDefaultInstance()
								.apply(container, contained) <= levenshteinDistance));
	}
}
