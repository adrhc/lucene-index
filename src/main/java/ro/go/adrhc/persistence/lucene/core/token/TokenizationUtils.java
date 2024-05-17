package ro.go.adrhc.persistence.lucene.core.token;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
public class TokenizationUtils {
	private final Analyzer analyzer;

	public Set<String> tokenizeAll(@NonNull Collection<String> words) throws IOException {
		Set<String> result = new HashSet<>();
		for (String w : words) {
			result.addAll(tokenize(w));
		}
		return result;
	}

	public Set<String> tokenize(String text) throws IOException {
		try (TokenStream tokenStream = analyzer.tokenStream(null, text)) {
			return new HashSet<>(doTokenize(tokenStream));
		}
	}

	public List<String> tokenizeAsList(String text) throws IOException {
		try (TokenStream tokenStream = analyzer.tokenStream(null, text)) {
			return doTokenize(tokenStream);
		}
	}

	public String normalize(Enum<?> field, String text) {
		return normalize(field.name(), text);
	}

	public String normalize(String fieldName, String text) {
		return analyzer.normalize(fieldName, text).utf8ToString();
	}

	private List<String> doTokenize(TokenStream tokenStream) throws IOException {
		tokenStream.reset();
		List<String> tokens = new ArrayList<>();
		CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
		while (tokenStream.incrementToken()) {
			tokens.add(termAttribute.toString());
		}
		tokenStream.end();
		return tokens;
	}
}
