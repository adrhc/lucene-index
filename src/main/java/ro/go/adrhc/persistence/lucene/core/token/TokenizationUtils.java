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

	public Set<String> wordsToTokenSet(@NonNull Collection<String> words) throws IOException {
		Set<String> result = new HashSet<>();
		for (String w : words) {
			result.addAll(textToTokenSet(w));
		}
		return result;
	}

	public Set<String> textToTokenSet(String text) throws IOException {
		try (TokenStream tokenStream = analyzer.tokenStream(null, text)) {
			return textToTokenSet(tokenStream);
		}
	}

	public List<String> textToTokenList(String text) throws IOException {
		try (TokenStream tokenStream = analyzer.tokenStream(null, text)) {
			return textToTokenList(tokenStream);
		}
	}

	public String normalize(Enum<?> field, String text) {
		return normalize(field.name(), text);
	}

	public String normalize(String fieldName, String text) {
		return analyzer.normalize(fieldName, text).utf8ToString();
	}

	private List<String> textToTokenList(TokenStream tokenStream) throws IOException {
		List<String> list = new ArrayList<>();
		addTokensToCollection(list, tokenStream);
		return list;
	}

	private Set<String> textToTokenSet(TokenStream tokenStream) throws IOException {
		Set<String> set = new HashSet<>();
		addTokensToCollection(set, tokenStream);
		return set;
	}

	private void addTokensToCollection(Collection<String> collection, TokenStream tokenStream)
			throws IOException {
		tokenStream.reset();
		CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
		while (tokenStream.incrementToken()) {
			collection.add(termAttribute.toString());
		}
		tokenStream.end();
	}
}
