package ro.go.adrhc.persistence.lucene.operations.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.typed.read.ScoreAndValue;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class BestMatchSearchServiceImpl<T> implements BestMatchSearchService<T> {
	private final TypedIndexReaderTemplate<?, T> typedIndexReaderTemplate;

	public static <T> BestMatchSearchServiceImpl<T> of(TypedIndexReaderParams<T> params) {
		return new BestMatchSearchServiceImpl<>(TypedIndexReaderTemplate.create(params));
	}

	@Override
	public Optional<T> findBestMatch(Query query) throws IOException {
		return typedIndexReaderTemplate.useReader(
			r -> r.findMany(query, 1)
				.map(ScoreAndValue::value).findFirst());
	}

	@Override
	public List<QueryAndValue<T>> findBestMatches(
		Collection<? extends Query> queries) throws IOException {
		return typedIndexReaderTemplate.useReader(r -> {
			List<QueryAndValue<T>> result = new ArrayList<>();
			for (Query query : queries) {
				r.findMany(query, 1)
					.map(sv -> new QueryAndValue<>(query, sv.value()))
					.findFirst()
					.ifPresent(result::add);
			}
			return result;
		});
	}
}
