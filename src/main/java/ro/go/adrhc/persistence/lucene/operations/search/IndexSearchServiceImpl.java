package ro.go.adrhc.persistence.lucene.operations.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderTemplate;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class IndexSearchServiceImpl<T> implements IndexSearchService<T> {
	private final BestMatchSearchService<T> bestMatchSearchService;
	private final SearchReduceService<T> searchReduceService;
	private final SearchManyService<T> searchManyService;

	public static <T> IndexSearchServiceImpl<T>
	create(IndexSearchServiceParams<T> params) {
		BestMatchSearchService<T> bestMatchSearchService =
				BestMatchSearchServiceImpl.of(params.oneHitIndexReaderParams());
		SearchReduceService<T> searchReduceService = new SearchReduceServiceImpl<>(
				HitsLimitedIndexReaderTemplate.create(params), // limited to params.numHits
				params.getSearchResultFilter());
		SearchManyService<T> searchManyService = new SearchManyServiceImpl<>(
				HitsLimitedIndexReaderTemplate.create(params.allHitsTypedIndexReaderParams()),
				params.getSearchResultFilter());
		return new IndexSearchServiceImpl<>(bestMatchSearchService,
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
	public ScoreDocAndValues<T> findMany(Query query, int hitsCount, Sort sort) throws IOException {
		return searchManyService.findMany(query, hitsCount, sort);
	}

	@Override
	public ScoreDocAndValues<T> findMany(Query query, Sort sort) throws IOException {
		return searchManyService.findMany(query, sort);
	}

	@Override
	public ScoreDocAndValues<T> findMany(Query query, int hitsCount) throws IOException {
		return searchManyService.findMany(query, hitsCount);
	}

	@Override
	public List<T> findMany(Query query) throws IOException {
		return searchManyService.findMany(query);
	}

	@Override
	public ScoreDocAndValues<T> findManyAfter(
			ScoreDoc after, Query query, Sort sort) throws IOException {
		return searchManyService.findManyAfter(after, query, sort);
	}

	@Override
	public ScoreDocAndValues<T> findManyAfter(
			ScoreDoc after, Query query, int hitsCount, Sort sort)
			throws IOException {
		return searchManyService.findManyAfter(after, query, hitsCount, sort);
	}
}
