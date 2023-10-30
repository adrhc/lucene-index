package ro.go.adrhc.persistence.lucene.index.domain.queries;

import lombok.NonNull;
import org.apache.lucene.search.Query;

import java.util.Optional;

public interface SearchedToQueryConverter<S> {
	@NonNull Optional<Query> convert(S searched);
}
