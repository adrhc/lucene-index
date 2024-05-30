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

@RequiredArgsConstructor
@Slf4j
public class DefaultSearchReduceService<T> implements SearchReduceService<T> {
	private final TypedIndexReaderTemplate<?, T> indexReaderTemplate;
	private final SearchResultFilter<T> searchResultFilter;

	@Override
	public Optional<T> findBestMatch(BestMatchingStrategy<T> bestMatchingStrategy, Query query)
			throws IOException {
		return indexReaderTemplate
				.useReader(reader -> doFindBestMatch(bestMatchingStrategy, query, reader))
				.map(QueryAndValue::value);
	}

	@Override
	public List<QueryAndValue<T>> findBestMatches(
			BestMatchingStrategy<T> bestMatchingStrategy,
			Collection<? extends Query> queries) throws IOException {
		return indexReaderTemplate.useReader(reader ->
				doFindBestMatches(bestMatchingStrategy, queries, reader));
	}

	protected List<QueryAndValue<T>>
	doFindBestMatches(BestMatchingStrategy<T> bestMatchingStrategy,
			Collection<? extends Query> queries, TypedIndexReader<?, T> reader) throws IOException {
		List<QueryAndValue<T>> result = new ArrayList<>();
		for (Query query : queries) {
			doFindBestMatch(bestMatchingStrategy, query, reader).ifPresent(result::add);
		}
		return result;
	}

	protected Optional<QueryAndValue<T>> doFindBestMatch(
			BestMatchingStrategy<T> bestMatchingStrategy,
			Query query, TypedIndexReader<?, T> reader) throws IOException {
		Stream<TypedSearchResult<T>> allMatches = doFindAllMatches(query, reader)
				.map(sat -> new TypedSearchResult<>(query, sat));
		return bestMatchingStrategy.bestMatch(allMatches).map(QueryAndValue::of);
	}

	protected Stream<ScoreAndValue<T>> doFindAllMatches(
			Query query, TypedIndexReader<?, T> reader) throws IOException {
		// log.debug("\nQuery used to search:\n{}", query);
		return reader.findMany(query).filter(searchResultFilter::filter);
	}
}
