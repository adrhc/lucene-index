package ro.go.adrhc.persistence.lucene.core.bare.analysis;

import lombok.*;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class TokenizerProperties {
	private static final int DEFAULT_MIN_TOKEN_LENGTH = 2;
	private int minTokenLength = DEFAULT_MIN_TOKEN_LENGTH;
	private List<String> fixedPatternsNotToIndex = List.of();
	private List<String> regexPatternsNotToIndex = List.of();
	private Map<String, String> charactersToReplaceBeforeIndexing = Map.of();
	private PatternsAndReplacement regexPatternsAndReplacement = PatternsAndReplacement.EMPTY;

	public void setMinTokenLength(Integer minTokenLength) {
		this.minTokenLength = minTokenLength == null ?
			DEFAULT_MIN_TOKEN_LENGTH : minTokenLength;
	}
}
