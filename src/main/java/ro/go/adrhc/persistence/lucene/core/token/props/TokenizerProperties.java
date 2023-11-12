package ro.go.adrhc.persistence.lucene.core.token.props;

import lombok.*;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class TokenizerProperties {
	private int minTokenLength;
	private List<String> fixedPatternsNotToIndex = List.of();
	private List<String> regexPatternsNotToIndex = List.of();
	private Map<String, String> charactersToReplaceBeforeIndexing = Map.of();
	private PatternsAndReplacement regexPatternsAndReplacement = PatternsAndReplacement.EMPTY;
}
