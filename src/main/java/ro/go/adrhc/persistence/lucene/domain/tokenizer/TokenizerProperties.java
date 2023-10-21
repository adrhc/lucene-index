package ro.go.adrhc.persistence.lucene.domain.tokenizer;

import lombok.*;

import java.util.List;

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
}
