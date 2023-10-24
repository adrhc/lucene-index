package ro.go.adrhc.persistence.lucene.index.core.tokenizer;

import java.util.Collection;
import java.util.Set;

public record PatternsAndReplacement(String replacement, Collection<String> patterns) {
	public static PatternsAndReplacement EMPTY = new PatternsAndReplacement(null, Set.of());
}
