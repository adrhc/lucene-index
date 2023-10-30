package ro.go.adrhc.persistence.lucene.index.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReader;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;

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
 * @param <F> means found
 */
@RequiredArgsConstructor
@Slf4j
public class IndexSearchService<F> {
	private final DocumentIndexReaderTemplate documentIndexReaderTemplate;
	private final IndexSearchResultFactory<F> toFoundConverter;
	private final BestMatchingStrategy<F> bestMatchingStrategy;
	private final SearchResultFilter<F> searchResultFilter;

	public List<F> findAllMatches(Query query) throws IOException {
		return documentIndexReaderTemplate.useReader(indexReader ->
				doFindAllMatches(indexReader, query).toList());
	}

	public Optional<F> findBestMatch(Query query) throws IOException {
		return documentIndexReaderTemplate.useReader(
				indexReader -> doFindBestMatch(indexReader, query));
	}

	public List<F> findBestMatches(Collection<? extends Query> queries) throws IOException {
		return documentIndexReaderTemplate.useReader(
				indexReader -> doFindBestMatches(indexReader, queries));
	}

	protected List<F> doFindBestMatches(DocumentIndexReader indexReader, Collection<? extends Query> queries) throws IOException {
		List<F> fList = new ArrayList<>();
		for (Query query : queries) {
			doFindBestMatch(indexReader, query).ifPresent(fList::add);
		}
		return fList;
	}

	protected Optional<F> doFindBestMatch(DocumentIndexReader indexReader, Query query) throws IOException {
		return bestMatchingStrategy.bestMatch(doFindAllMatches(indexReader, query));
	}

	protected Stream<F> doFindAllMatches(DocumentIndexReader indexReader, Query query) throws IOException {
		// log.debug("\nQuery used to search:\n{}", query);
		return indexReader.search(query)
				.map(scoreAndDocument -> toFoundConverter.create(query, scoreAndDocument))
				.flatMap(Optional::stream)
				.filter(searchResultFilter::filter);
	}
}
