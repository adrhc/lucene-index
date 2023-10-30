package ro.go.adrhc.persistence.lucene.typedindex.domain.seach;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TypedSearchResult<S, F> {
	private final float score;
	private final S searched;
	private final F found;
}
