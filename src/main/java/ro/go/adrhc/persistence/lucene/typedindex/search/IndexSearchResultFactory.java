package ro.go.adrhc.persistence.lucene.typedindex.search;

import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.read.ScoreAndDocument;

import java.util.Optional;

public interface IndexSearchResultFactory<F> {
	Optional<F> create(Query query, ScoreAndDocument scoreAndDocument);
}
