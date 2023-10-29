package ro.go.adrhc.persistence.lucene.index.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReader;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This is part of infrastructure layer because contains methods directly using lucene API.
 * It is a service because one could swap it with other implementation without any other change.
 *
 * @param <S> means searched
 * @param <F> means found
 */
@RequiredArgsConstructor
@Slf4j
public class IndexSearchService<S, F> {
	private final DocumentIndexReaderTemplate documentIndexReaderTemplate;
	private final SearchedToQueryConverter<S> toQueryConverter;
	private final IndexSearchResultFactory<S, F> toFoundConverter;
	private final BestMatchingStrategy<F> bestMatchingStrategy;
	private final SearchResultFilter<F> searchResultFilter;

	public List<F> findAllMatches(S searched) throws IOException {
		return documentIndexReaderTemplate.useReader(indexReader ->
				doFindAllMatches(indexReader, searched).toList());
	}

	public Optional<F> findBestMatch(S searched) throws IOException {
		return documentIndexReaderTemplate.useReader(
				indexReader -> doFindBestMatch(indexReader, searched));
	}

	public List<F> findBestMatches(Collection<S> searchedItems) throws IOException {
		return documentIndexReaderTemplate.useReader(
				indexReader -> doFindBestMatches(indexReader, searchedItems).toList());
	}

	protected Stream<F> doFindBestMatches(DocumentIndexReader indexReader, Collection<S> searchedItems) {
		return searchedItems.stream()
				.map(searched -> doSafelyFindBestMatch(indexReader, searched))
				.flatMap(Optional::stream);
	}

	protected Optional<F> doSafelyFindBestMatch(DocumentIndexReader indexReader, S searched) {
		try {
			return doFindBestMatch(indexReader, searched);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return Optional.empty();
		}
	}

	protected Optional<F> doFindBestMatch(DocumentIndexReader indexReader, S searched) throws IOException {
		return bestMatchingStrategy.bestMatch(doFindAllMatches(indexReader, searched));
	}

	protected Stream<F> doFindAllMatches(DocumentIndexReader indexReader, S searched) throws IOException {
		// log.debug("\nSearching:\n{}", searched);
		Optional<Query> optionalQuery = toQueryConverter.convert(searched);
		if (optionalQuery.isEmpty()) {
			throw new IOException("Failed to create the lucene query!");
		}
		return doFindAllMatches(indexReader, searched, optionalQuery.get());
	}

	protected Stream<F> doFindAllMatches(DocumentIndexReader indexReader, S searchedItem, Query query) throws IOException {
		// log.debug("\nQuery used to search:\n{}", query);
		return indexReader.search(query)
				.map(scoreAndDocument -> toFoundConverter.create(searchedItem, scoreAndDocument))
				.flatMap(Optional::stream)
				.filter(searchResultFilter::filter);
	}
}
