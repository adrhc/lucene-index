package ro.go.adrhc.persistence.lucene.lib;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Component;
import ro.go.adrhc.util.ObjectUtils;

import java.util.Objects;
import java.util.stream.Stream;

import static ro.go.adrhc.util.fn.SneakyBooleanSupplierUtils.failToFalse;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenStreamToStreamConverter {
	private static final Object NULL = new Object();

	/**
	 * Converts a TokenStream to a Stream of Strings filtering out the nulls.
	 */
	public Stream<String> convert(TokenStream tokenStream) {
		CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
		return Stream.generate(() -> {
				if (failToFalse(tokenStream::incrementToken)) {
					return termAttribute.toString();
				} else {
					return NULL;
				}
			})
			.takeWhile(it -> it != NULL)
			.filter(Objects::nonNull)
			.map(ObjectUtils::cast);
	}
}
