package ro.go.adrhc.persistence.lucene.tokenizer;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class TokenizerProperties {
	private int minTokenLength;
	private List<String> fixedPatternsNotToIndex;
	private List<String> regexPatternsNotToIndex;
	private List<String[]> charactersToReplaceBeforeIndexing;
}
