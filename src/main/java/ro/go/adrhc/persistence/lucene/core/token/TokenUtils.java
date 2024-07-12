package ro.go.adrhc.persistence.lucene.core.token;

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
		Set<String> containerTokens = tokenizationUtils.textToTokenSet(containing);
		Set<String> containedTokens = tokenizationUtils.textToTokenSet(contained);
		return containedDiffersSlightly(levenshteinDistance, containerTokens, containedTokens);
	}

	private boolean containedDiffersSlightly(int levenshteinDistance,
			Set<String> containerTokens, Set<String> containedTokens) {
		return SetUtils.difference(containedTokens, containerTokens)
				.stream().allMatch(contained -> tokenMatchSlightlyDifferent(
						levenshteinDistance, containerTokens, contained));
	}

	private static boolean tokenMatchSlightlyDifferent(int levenshteinDistance,
			Set<String> containerTokens, CharSequence contained) {
		return containerTokens.stream().anyMatch(container ->
				leLevenshteinDistance(levenshteinDistance, contained, container));
	}

	private static boolean leLevenshteinDistance(
			int levenshteinDistance, CharSequence first, CharSequence second) {
		return LevenshteinDistance.getDefaultInstance().apply(first, second) <= levenshteinDistance;
	}

	private static Integer levenshteinDistance(CharSequence first, CharSequence second) {
		return LevenshteinDistance.getDefaultInstance().apply(first, second);
	}
}
