package ro.go.adrhc.persistence.lucene.index.core.tokenizer;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.miscellaneous.RemoveDuplicatesTokenFilter;
import org.apache.lucene.analysis.miscellaneous.TrimFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import ro.go.adrhc.util.text.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static ro.go.adrhc.persistence.lucene.index.core.analysis.CharFilterFactory.*;

@Slf4j
public record LuceneTokenizer(Analyzer analyzer, TokenizerProperties properties) {
	/**
	 * This works well as singleton only when StandardAnalyzer is thread safe!
	 */
	public static LuceneTokenizer standardTokenizer(TokenizerProperties tokenizerProperties) {
		return new LuceneTokenizer(new StandardAnalyzer(), tokenizerProperties);
	}

	public boolean containedDiffersSlightly(int levenshteinDistance,
			Set<String> containerTokens, String contained) throws IOException {
		Set<String> containedTokens = tokenize(contained);
		return containedDiffersSlightly(levenshteinDistance, containerTokens, containedTokens);
	}

	public Set<String> tokenize(@NonNull Collection<String> words) throws IOException {
		Set<String> result = new HashSet<>();
		for (String w : words) {
			result.addAll(tokenize(w));
		}
		return result;
	}

	public Set<String> tokenize(String text) throws IOException {
		if (StringUtils.isBlank(text)) {
			return Set.of();
		}
		try (TokenStream tokenStream = tokenStreamOf(text)) {
			return doTokenize(tokenStream);
		}
	}

	private Set<String> doTokenize(TokenStream tokenStream) throws IOException {
		tokenStream.reset();
		CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
		Set<String> tokens = new HashSet<>();
		while (tokenStream.incrementToken()) {
			String token = termAttribute.toString();
			if (token.length() < properties.getMinTokenLength()) {
				continue;
			}
			tokens.add(token);
		}
		tokenStream.end();
		return tokens;
	}

	private TokenStream tokenStreamOf(String string) {
		Reader reader = new StringReader(string);
		reader = mappingCharFilter(reader, properties.getCharactersToReplaceBeforeIndexing());
		reader = textRemoveCharFilter(reader, properties.getFixedPatternsNotToIndex());
		reader = patternRemoveCharFilter(reader, properties.getRegexPatternsNotToIndex());
		TokenStream analyzerTokenStream = analyzer.tokenStream(null, reader);
		// ASCIIFoldingFilter: șțâăî = staii
		return new RemoveDuplicatesTokenFilter(
				new LowerCaseFilter(
						new TrimFilter(
								new ASCIIFoldingFilter(analyzerTokenStream))));
	}

	private boolean containedDiffersSlightly(int levenshteinDistance,
			Set<String> containerTokens,
			Set<String> containedTokens) {
		return SetUtils.difference(containedTokens, containerTokens).stream()
				.allMatch(contained -> containerTokens.stream()
						.anyMatch(container -> LevenshteinDistance.getDefaultInstance()
								.apply(container, contained) <= levenshteinDistance));
	}
}
