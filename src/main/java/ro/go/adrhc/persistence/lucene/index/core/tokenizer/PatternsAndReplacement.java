package ro.go.adrhc.persistence.lucene.index.core.tokenizer;

import java.util.Collection;

public record PatternsAndReplacement(String replacement, Collection<String> patterns) {
}
