package ro.go.adrhc.persistence.lucene.index.core.analysis;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.miscellaneous.RemoveDuplicatesTokenFilter;
import org.apache.lucene.analysis.miscellaneous.TrimFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties;

import java.io.Reader;

import static org.apache.lucene.analysis.standard.StandardTokenizer.MAX_TOKEN_LENGTH_LIMIT;
import static ro.go.adrhc.persistence.lucene.index.core.analysis.CharFilterFactory.*;

@RequiredArgsConstructor
public class AnalyzerFactory {
	private final TokenizerProperties properties;

	public Analyzer create() {
		return new Analyzer() {
			@Override
			protected TokenStreamComponents createComponents(String fieldName) {
				final StandardTokenizer src = new StandardTokenizer();
				src.setMaxTokenLength(MAX_TOKEN_LENGTH_LIMIT);
				TokenStream tok = AnalyzerFactory.this.trimAsciiFoldingLowerLengthLimitDupsRmTokenStream(src);
				return new TokenStreamComponents(r -> {
					src.setMaxTokenLength(MAX_TOKEN_LENGTH_LIMIT);
					src.setReader(charsSwapRmTextsRmPatternsCharFilter(r));
				}, tok);
			}

			/*@Override
			protected Reader initReaderForNormalization(String fieldName, Reader reader) {
				return charFilter(reader);
			}

			@Override
			protected TokenStream normalize(String fieldName, TokenStream in) {
				return AnalyzerFactory.this.tokenStream(in);
			}*/

			@Override
			protected TokenStream normalize(String fieldName, TokenStream in) {
				return new LowerCaseFilter(in);
			}
		};
	}

	/**
	 * Chained CharFilter(s):
	 * - MappingCharFilter
	 * - PatternReplaceCharFilter using fixed test
	 * - PatternReplaceCharFilter using regex patterns
	 */
	private Reader charsSwapRmTextsRmPatternsCharFilter(Reader reader) {
		reader = mappingCharFilter(reader, properties.getCharactersToReplaceBeforeIndexing());
		reader = textRemoveCharFilter(reader, properties.getFixedPatternsNotToIndex());
		return patternRemoveCharFilter(reader, properties.getRegexPatternsNotToIndex());
	}

	/**
	 * Chained TokenStream(s):
	 * - TrimFilter
	 * - ASCIIFoldingFilter
	 * - LowerCaseFilter
	 * - LengthFilter
	 * - RemoveDuplicatesTokenFilter
	 */
	private TokenStream trimAsciiFoldingLowerLengthLimitDupsRmTokenStream(TokenStream tokenStream) {
		return new RemoveDuplicatesTokenFilter(
				new LengthFilter(
						new LowerCaseFilter(
								new ASCIIFoldingFilter(
										new TrimFilter(tokenStream))),
						properties.getMinTokenLength(), MAX_TOKEN_LENGTH_LIMIT
				)
		);
	}
}
