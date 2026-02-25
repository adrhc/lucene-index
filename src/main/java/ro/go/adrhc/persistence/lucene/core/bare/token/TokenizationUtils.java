package ro.go.adrhc.persistence.lucene.core.bare.token;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import ro.go.adrhc.persistence.lucene.lib.TokenStreamToStreamConverter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TokenizationUtils {
	private final TokenStreamToStreamConverter tokenStreamToStream;
	private final Analyzer analyzer;

	public static TokenizationUtils of(Analyzer analyzer) {
		return new TokenizationUtils(new TokenStreamToStreamConverter(), analyzer);
	}

	public Set<String> textCollectionToTokenSet(@NonNull Collection<String> words) throws IOException {
		Set<String> tokens = new HashSet<>();
		for (String w : words) {
			tokens.addAll(textToTokenSet(w));
		}
		return tokens;
	}

	public Set<String> textToTokenSet(String text) throws IOException {
		return useTokenStream(ts -> tokenStreamToStream.convert(ts).collect(Collectors.toSet()), text);
	}

	/**
	 * @return a sorted list of tokens obtained from the given text
	 */
	public List<String> textToTokenList(String text) throws IOException {
		return useTokenStream(ts -> tokenStreamToStream.convert(ts).distinct().toList(), text);
	}

	public String normalize(String fieldName, String text) {
		return analyzer.normalize(fieldName, text).utf8ToString();
	}

	private <T> T useTokenStream(Function<TokenStream, T> fn, String text) throws IOException {
		try (TokenStream tokenStream = analyzer.tokenStream(null, text)) {
			tokenStream.reset();
			T t = fn.apply(tokenStream);
			tokenStream.end();
			return t;
		}
	}
}
