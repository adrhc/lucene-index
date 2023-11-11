package ro.go.adrhc.persistence.lucene.typedindex.search.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TypedSearchResult<S, F> implements SearchResult<S, F> {
	private final S searched;
	private final float score;
	private final F found;
}
