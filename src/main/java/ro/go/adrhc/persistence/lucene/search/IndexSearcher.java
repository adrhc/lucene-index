package ro.go.adrhc.persistence.lucene.search;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.read.DocumentIndexReader;
import ro.go.adrhc.persistence.lucene.read.DocumentIndexReaderTemplate;

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
 * @param <S> means searched
 * @param <F> means found
 */
@RequiredArgsConstructor
@Slf4j
public class IndexSearcher<S, F> {
	private final DocumentIndexReaderTemplate documentIndexReaderTemplate;
	private final SneakyFunction<S, Query, IOException> toQueryConverter;
	private final IndexSearchResultFactory<S, F> toFoundConverter;
	private final BestMatchingStrategy<F> bestMatchingStrategy;

	public List<F> findAllMatches(S searchedItem) throws IOException {
		return documentIndexReaderTemplate.useReader(indexReader ->
				findAllMatches(indexReader, searchedItem).toList());
	}

	public Optional<F> findBestMatch(S searchedItem) throws IOException {
		return documentIndexReaderTemplate.useReader(indexReader
				-> bestMatchingStrategy.bestMatch(findAllMatches(indexReader, searchedItem)));
	}

	public List<F> findBestMatches(Collection<S> searchedItems) throws IOException {
		return documentIndexReaderTemplate.useReader(
				indexReader -> doFindBestMatches(searchedItems, indexReader));
	}

	protected List<F> doFindBestMatches(Collection<S> searchedItems,
			DocumentIndexReader indexReader) throws IOException {
		List<F> result = new ArrayList<>();
		for (S searched : searchedItems) {
			Stream<F> findings = findAllMatches(indexReader, searched);
			Optional<F> bestMatch = bestMatchingStrategy.bestMatch(findings);
			bestMatch.ifPresent(result::add);
		}
		return result;
	}

	protected Stream<F> findAllMatches(
			DocumentIndexReader indexReader, S searchedItem) throws IOException {
		// log.debug("\nSearching:\n{}", searchedItem);
		Query query = toQueryConverter.apply(searchedItem);
		// log.debug("\nquery used to search:\n{}", query);
		return indexReader.search(query).stream()
				.map(scoreAndDocument -> toFoundConverter
						.create(searchedItem, scoreAndDocument));
	}
}
