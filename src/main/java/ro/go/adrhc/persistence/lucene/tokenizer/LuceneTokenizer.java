package ro.go.adrhc.persistence.lucene.tokenizer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.charfilter.MappingCharFilter;
import org.apache.lucene.analysis.charfilter.NormalizeCharMap;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.pattern.PatternReplaceCharFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import ro.go.adrhc.util.text.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

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

	public Set<String> tokenize(String text) throws IOException {
		if (StringUtils.isBlank(text)) {
			return Set.of();
		}
		TokenStream tokenStream = tokenStreamOf(text);
		tokenStream.reset();
		CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
		Set<String> tokens = new HashSet<>();
		try {
			while (tokenStream.incrementToken()) {
				String token = termAttribute.toString();
				if (token.length() < properties.getMinTokenLength()) {
					continue;
				}
				tokens.add(token);
			}
			return tokens;
		} finally {
			endAndClose(tokenStream);
		}
	}

	public Set<String> tokenize(@NonNull Collection<String> words) throws IOException {
		Set<String> result = new HashSet<>();
		for (String w : words) {
			result.addAll(tokenize(w));
		}
		return result;
	}

	private TokenStream tokenStreamOf(String string) {
		// LowerCaseFilter is already included by StandardAnalyzer
		NormalizeCharMap.Builder normalizeCharMapBuilder = new NormalizeCharMap.Builder();
		properties.getCharactersToReplaceBeforeIndexing()
				.forEach(it -> normalizeCharMapBuilder.add(it[0], it[1]));
		MappingCharFilter mappingCharFilter =
				new MappingCharFilter(normalizeCharMapBuilder.build(), new StringReader(string));

		@AllArgsConstructor
		class ReaderHolder {
			Reader reader;
		}
		ReaderHolder patternExclusionsReader = new ReaderHolder(mappingCharFilter);

		properties.getFixedPatternsNotToIndex().forEach(s -> patternExclusionsReader.reader =
				new PatternReplaceCharFilter(
						Pattern.compile(s, CASE_INSENSITIVE | Pattern.LITERAL),
						"", patternExclusionsReader.reader));
		properties.getRegexPatternsNotToIndex().forEach(s -> patternExclusionsReader.reader =
				new PatternReplaceCharFilter(Pattern.compile(s, CASE_INSENSITIVE),
						" ", patternExclusionsReader.reader));

		TokenStream analyzerTokenStream = analyzer.tokenStream(null, patternExclusionsReader.reader);

		// șțâăî = staii
		return new ASCIIFoldingFilter(analyzerTokenStream);
	}

	private boolean containedDiffersSlightly(int levenshteinDistance,
			Set<String> containerTokens,
			Set<String> containedTokens) {
		return SetUtils.difference(containedTokens, containerTokens).stream()
				.allMatch(contained -> containerTokens.stream()
						.anyMatch(container -> LevenshteinDistance.getDefaultInstance()
								.apply(container, contained) <= levenshteinDistance));
	}

	private void endAndClose(TokenStream tokenStream) {
		try {
			tokenStream.end();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		try {
			tokenStream.close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
