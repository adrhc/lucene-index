package ro.go.adrhc.persistence.lucene.typedindex.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.read.DocumentsIndexReader;
import ro.go.adrhc.persistence.lucene.core.read.DocumentsIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;

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
public class TypedIndexSearchService<F> implements IndexSearchService<F> {
	private final DocumentsIndexReaderTemplate documentsIndexReaderTemplate;
	private final IndexSearchResultFactory<F> toFoundConverter;
	private final SearchResultFilter<F> searchResultFilter;

	@Override
	public List<F> findAllMatches(Query query) throws IOException {
		return documentsIndexReaderTemplate.useReader(indexReader ->
				doFindAllMatches(indexReader, query).toList());
	}

	@Override
	public Optional<F> findBestMatch(Query query) throws IOException {
		return findBestMatch(Stream::findFirst, query);
	}

	@Override
	public List<F> findBestMatches(Collection<? extends Query> queries) throws IOException {
		return documentsIndexReaderTemplate.useReader(indexReader ->
				doFindBestMatches(Stream::findFirst, queries, indexReader));
	}

	@Override
	public Optional<F> findBestMatch(BestMatchingStrategy<F> bestMatchingStrategy, Query query) throws IOException {
		return documentsIndexReaderTemplate.useReader(indexReader ->
				bestMatchingStrategy.bestMatch(doFindAllMatches(indexReader, query)));
	}

	@Override
	public List<F> findBestMatches(BestMatchingStrategy<F> bestMatchingStrategy,
			Collection<? extends Query> queries) throws IOException {
		return documentsIndexReaderTemplate.useReader(indexReader ->
				doFindBestMatches(bestMatchingStrategy, queries, indexReader));
	}

	protected List<F> doFindBestMatches(BestMatchingStrategy<F> bestMatchingStrategy,
			Collection<? extends Query> queries, DocumentsIndexReader indexReader) throws IOException {
		List<F> fList = new ArrayList<>();
		for (Query query : queries) {
			bestMatchingStrategy.bestMatch(doFindAllMatches(indexReader, query)).ifPresent(fList::add);
		}
		return fList;
	}

	protected Stream<F> doFindAllMatches(DocumentsIndexReader indexReader, Query query) throws IOException {
		// log.debug("\nQuery used to search:\n{}", query);
		return indexReader.search(query)
				.map(scoreAndDocument -> toFoundConverter.create(query, scoreAndDocument))
				.flatMap(Optional::stream)
				.filter(searchResultFilter::filter);
	}
}
