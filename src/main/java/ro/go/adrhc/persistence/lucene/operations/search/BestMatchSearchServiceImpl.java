package ro.go.adrhc.persistence.lucene.operations.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.typed.read.OneHitIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.read.OneHitIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.core.bare.read.ScoreDocAndValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class BestMatchSearchServiceImpl<T> implements BestMatchSearchService<T> {
	private final OneHitIndexReaderTemplate<T> oneHitIndexReaderTemplate;

	public static <T> BestMatchSearchServiceImpl<T> of(OneHitIndexReaderParams<T> params) {
		return new BestMatchSearchServiceImpl<>(OneHitIndexReaderTemplate.create(params));
	}

	@Override
	public Optional<T> findBestMatch(Query query) throws IOException {
		return oneHitIndexReaderTemplate.useOneHitReader(
			r -> r.findFirst(query).map(ScoreDocAndValue::value));
	}

	@Override
	public List<QueryAndValue<T>> findBestMatches(
		Collection<? extends Query> queries) throws IOException {
		return oneHitIndexReaderTemplate.useOneHitReader(r -> {
			List<QueryAndValue<T>> result = new ArrayList<>();
			for (Query query : queries) {
				r.findFirst(query)
					.map(sv -> new QueryAndValue<>(query, sv.value()))
					.ifPresent(result::add);
			}
			return result;
		});
	}
}
