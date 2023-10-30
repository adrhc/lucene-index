package ro.go.adrhc.persistence.lucene.index.search;

import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.core.read.ScoreAndDocument;

import java.util.Optional;

public interface IndexSearchResultFactory<F> {
	Optional<F> create(Query query, ScoreAndDocument scoreAndDocument);
}
