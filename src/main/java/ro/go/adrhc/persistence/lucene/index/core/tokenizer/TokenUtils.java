package ro.go.adrhc.persistence.lucene.index.core.tokenizer;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
public class TokenUtils {
	private final TokenizationUtils tokenizationUtils;

	public boolean containedDiffersSlightly(int levenshteinDistance,
			String containing, String contained) throws IOException {
		Set<String> containerTokens = tokenizationUtils.tokenize(containing);
		Set<String> containedTokens = tokenizationUtils.tokenize(contained);
		return containedDiffersSlightly(levenshteinDistance, containerTokens, containedTokens);
	}

	private boolean containedDiffersSlightly(int levenshteinDistance,
			Set<String> containerTokens, Set<String> containedTokens) {
		return SetUtils.difference(containedTokens, containerTokens).stream()
				.allMatch(contained -> containerTokens.stream()
						.anyMatch(container -> LevenshteinDistance.getDefaultInstance()
								.apply(container, contained) <= levenshteinDistance));
	}
}
