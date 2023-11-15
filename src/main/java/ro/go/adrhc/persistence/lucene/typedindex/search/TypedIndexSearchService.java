package ro.go.adrhc.persistence.lucene.typedindex.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreAndValue;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReader;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This is part of infrastructure layer because contains methods directly using lucene API.
 * It is a service because one could swap it with other implementation without any other change.
 *
 * @param <T> means found
 */
@RequiredArgsConstructor
@Slf4j
public class TypedIndexSearchService<T> implements IndexSearchService<T> {
	private final TypedIndexReaderTemplate<?, T> indexReaderTemplate;
	private final SearchResultFilter<T> searchResultFilter;

	public static <T> TypedIndexSearchService<T>
	create(TypedIndexSearchServiceParams<T> params) {
		return new TypedIndexSearchService<>(
				TypedIndexReaderTemplate.create(params),
				params.getSearchResultFilter()
		);
	}

	@Override
	public List<T> findAllMatches(Query query) throws IOException {
		return indexReaderTemplate
				.useReader(reader -> doFindAllMatches(query, reader)
						.map(ScoreAndValue::value).toList());
	}

	@Override
	public Optional<T> findBestMatch(Query query) throws IOException {
		return findBestMatch(Stream::findFirst, query);
	}

	@Override
	public Optional<T> findBestMatch(BestMatchingStrategy<T> bestMatchingStrategy, Query query) throws IOException {
		return indexReaderTemplate
				.useReader(reader -> doFindBestMatch(bestMatchingStrategy, query, reader))
				.map(TypedSearchResult::value);
	}

	@Override
	public List<TypedSearchResult<T>> findBestMatches(
			Collection<? extends Query> queries) throws IOException {
		return findBestMatches(Stream::findFirst, queries);
	}

	@Override
	public List<TypedSearchResult<T>> findBestMatches(
			BestMatchingStrategy<T> bestMatchingStrategy,
			Collection<? extends Query> queries) throws IOException {
		return indexReaderTemplate.useReader(reader ->
				doFindBestMatches(bestMatchingStrategy, queries, reader));
	}

	protected List<TypedSearchResult<T>>
	doFindBestMatches(BestMatchingStrategy<T> bestMatchingStrategy,
			Collection<? extends Query> queries, TypedIndexReader<?, T> reader) throws IOException {
		List<TypedSearchResult<T>> result = new ArrayList<>();
		for (Query query : queries) {
			doFindBestMatch(bestMatchingStrategy, query, reader).ifPresent(result::add);
		}
		return result;
	}

	protected Optional<TypedSearchResult<T>> doFindBestMatch(
			BestMatchingStrategy<T> bestMatchingStrategy,
			Query query, TypedIndexReader<?, T> reader) throws IOException {
		Stream<TypedSearchResult<T>> allMatches = doFindAllMatches(query, reader)
				.map(sat -> new TypedSearchResult<>(query, sat));
		return bestMatchingStrategy.bestMatch(allMatches);
	}

	protected Stream<ScoreAndValue<T>> doFindAllMatches(
			Query query, TypedIndexReader<?, T> reader) throws IOException {
		// log.debug("\nQuery used to search:\n{}", query);
		return reader.findMany(query).filter(searchResultFilter::filter);
	}
}
