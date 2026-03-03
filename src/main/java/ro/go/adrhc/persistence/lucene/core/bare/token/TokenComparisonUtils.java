package ro.go.adrhc.persistence.lucene.core.bare.token;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
public class TokenComparisonUtils {
	private final TokenizationUtils tokenizationUtils;

	/**
	 * @return whether all tokens in textPiece are similar to some token in text
	 */
	public boolean isTextPieceSimilarToText(int levenshteinDistance,
		String text, String textPiece) throws IOException {
		Set<String> containerTokens = tokenizationUtils.textToTokenSet(text);
		Set<String> containedTokens = tokenizationUtils.textToTokenSet(textPiece);
		return areSimilar(levenshteinDistance, containerTokens, containedTokens);
	}

	/**
	 * @return whether all tokens in tokenSubSet are similar to some token in tokenSet
	 */
	private boolean areSimilar(int levenshteinDistance,
		Set<String> tokenSet, Set<String> tokenSubSet) {
		return SetUtils.difference(tokenSubSet, tokenSet)
			.stream().allMatch(contained -> containsSimilar(
				levenshteinDistance, tokenSet, contained));
	}

	/**
	 * @return whether token is similar to some token in tokenSet
	 */
	private static boolean containsSimilar(int levenshteinDistance,
		Set<String> tokenSet, CharSequence token) {
		return tokenSet.stream().anyMatch(container ->
			areSimilar(levenshteinDistance, token, container));
	}

	/**
	 * @return whether first and second are similar according to the given levenshteinDistance
	 */
	private static boolean areSimilar(
		int levenshteinDistance, CharSequence first, CharSequence second) {
		return LevenshteinDistance.getDefaultInstance().apply(first, second) <= levenshteinDistance;
	}
}
