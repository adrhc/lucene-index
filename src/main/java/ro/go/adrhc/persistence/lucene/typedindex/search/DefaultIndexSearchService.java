package ro.go.adrhc.persistence.lucene.typedindex.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderTemplate;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class DefaultIndexSearchService<T> implements IndexSearchService<T> {
	private final BestMatchSearchService<T> bestMatchSearchService;
	private final SearchReduceService<T> searchReduceService;
	private final SearchManyService<T> searchManyService;

	public static <T> DefaultIndexSearchService<T>
	create(IndexSearchServiceParams<T> params) {
		BestMatchSearchService<T> bestMatchSearchService =
				DefaultBestMatchSearchService.of(params.toOneHitIndexReaderParams());
		SearchReduceService<T> searchReduceService = new DefaultSearchReduceService<>(
				TypedIndexReaderTemplate.create(params), // limited to params.numHits
				params.getSearchResultFilter());
		SearchManyService<T> searchManyService = new DefaultSearchManyService<>(
				TypedIndexReaderTemplate.create(params.toAllHitsTypedIndexReaderParams()),
				params.getSearchResultFilter());
		return new DefaultIndexSearchService<>(bestMatchSearchService,
				searchReduceService, searchManyService);
	}

	@Override
	public Optional<T> findBestMatch(Query query) throws IOException {
		return bestMatchSearchService.findBestMatch(query);
	}

	@Override
	public List<QueryAndValue<T>> findBestMatches(Collection<? extends Query> queries)
			throws IOException {
		return bestMatchSearchService.findBestMatches(queries);
	}

	@Override
	public Optional<T> findBestMatch(BestMatchingStrategy<T> bestMatchingStrategy, Query query)
			throws IOException {
		return searchReduceService.findBestMatch(bestMatchingStrategy, query);
	}

	@Override
	public List<QueryAndValue<T>> findBestMatches(
			BestMatchingStrategy<T> bestMatchingStrategy,
			Collection<? extends Query> queries) throws IOException {
		return searchReduceService.findBestMatches(bestMatchingStrategy, queries);
	}

	@Override
	public List<T> findMany(Query query, int hitsCount, Sort sort) throws IOException {
		return searchManyService.findMany(query, hitsCount, sort);
	}

	@Override
	public List<T> findAllMatches(Query query) throws IOException {
		return searchManyService.findAllMatches(query);
	}
}
