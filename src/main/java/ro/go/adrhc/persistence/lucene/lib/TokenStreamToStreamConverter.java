package ro.go.adrhc.persistence.lucene.lib;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.TokenStream;
import org.springframework.stereotype.Component;
import ro.go.adrhc.util.stream.StreamUtils;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenStreamToStreamConverter {
	private final TokenStreamToIteratorConverter tokenStreamToIteratorConverter;

	public static TokenStreamToStreamConverter of() {
		return new TokenStreamToStreamConverter(new TokenStreamToIteratorConverter());
	}

	/**
	 * Converts a TokenStream to a Stream of Strings filtering out the nulls.
	 * The returned Stream will be closed when the TokenStream is closed.
	 */
	public Stream<String> convert(TokenStream tokenStream) {
		return StreamUtils.stream(tokenStreamToIteratorConverter.convert(tokenStream));
	}
}
