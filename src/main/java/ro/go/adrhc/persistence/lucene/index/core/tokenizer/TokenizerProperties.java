package ro.go.adrhc.persistence.lucene.index.core.tokenizer;

import lombok.*;

import java.util.List;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class TokenizerProperties {
	private int minTokenLength;
	private List<String> fixedPatternsNotToIndex = List.of();
	private List<String> regexPatternsNotToIndex = List.of();
	private List<String[]> charactersToReplaceBeforeIndexing = List.of();
	private PatternsAndReplacement regexPatternsAndReplacement = PatternsAndReplacement.EMPTY;

	public static TokenizerProperties of(int minTokenLength) {
		TokenizerProperties tokenizerProperties = new TokenizerProperties();
		tokenizerProperties.setMinTokenLength(minTokenLength);
		return tokenizerProperties;
	}

	public static TokenizerProperties of(int minTokenLength,
			List<String[]> charactersToReplaceBeforeIndexing,
			PatternsAndReplacement regexPatternsAndReplacement) {
		TokenizerProperties tokenizerProperties = new TokenizerProperties();
		tokenizerProperties.setMinTokenLength(minTokenLength);
		tokenizerProperties.setCharactersToReplaceBeforeIndexing(charactersToReplaceBeforeIndexing);
		tokenizerProperties.setRegexPatternsAndReplacement(regexPatternsAndReplacement);
		return tokenizerProperties;
	}

	public static List<String[]> pairAllWithSpace(final String... left) {
		return Stream.of(left).map(TokenizerProperties::pairWithSpace).toList();
	}

	public static String[] pairWithSpace(String left) {
		return new String[]{left, " "};
	}
}
