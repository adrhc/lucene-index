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
				TokenStream tok = AnalyzerFactory.this.tokenStream(src);
				return new TokenStreamComponents(r -> {
					src.setMaxTokenLength(MAX_TOKEN_LENGTH_LIMIT);
					src.setReader(charFilter(r));
				}, tok);
			}

			@Override
			protected TokenStream normalize(String fieldName, TokenStream in) {
				return new LowerCaseFilter(in);
			}
		};
	}

	private Reader charFilter(Reader reader) {
		reader = mappingCharFilter(reader, properties.getCharactersToReplaceBeforeIndexing());
		reader = textRemoveCharFilter(reader, properties.getFixedPatternsNotToIndex());
		return patternRemoveCharFilter(reader, properties.getRegexPatternsNotToIndex());
	}

	private TokenStream tokenStream(TokenStream tokenStream) {
		return new RemoveDuplicatesTokenFilter(
				new LengthFilter(
						new LowerCaseFilter(new ASCIIFoldingFilter(new TrimFilter(tokenStream))),
						properties.getMinTokenLength(), MAX_TOKEN_LENGTH_LIMIT
				)
		);
	}
}
