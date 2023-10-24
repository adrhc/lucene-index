package ro.go.adrhc.persistence.lucene.index.core.tokenizer;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public record PatternsAndReplacement(String replacement, Collection<String> patterns) {
	public static PatternsAndReplacement EMPTY = new PatternsAndReplacement(null, Set.of());

	public static PatternsAndReplacement caseInsensitive(String replacement, String pattern) {
		return new PatternsAndReplacement(replacement, List.of("(?i)" + pattern));
	}
}
