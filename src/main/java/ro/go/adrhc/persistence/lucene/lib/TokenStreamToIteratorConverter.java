package ro.go.adrhc.persistence.lucene.lib;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

@Component
@Slf4j
public class TokenStreamToIteratorConverter {
	public Iterator<String> convert(TokenStream tokenStream) {
		CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
		return new Iterator<>() {
			private boolean hasNext;
			private boolean computed;
			private boolean finished;

			@Override
			public boolean hasNext() {
				if (!computed) {
					advance();
					computed = true;
				}
				return hasNext;
			}

			@Override
			public String next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				computed = false;
				return termAttribute.toString();
			}

			private void advance() {
				if (finished) {
					hasNext = false;
					return;
				}
				try {
					if (tokenStream.incrementToken()) {
						hasNext = true;
					} else {
						tokenStream.end();
						tokenStream.close();
						finished = true;
						hasNext = false;
					}
				} catch (IOException e) {
					finished = true;
					try {
						tokenStream.close();
					} catch (IOException ignored) {
						log.error(e.getMessage(), e);
					}
					throw new UncheckedIOException(e);
				}
			}
		};
	}
}
