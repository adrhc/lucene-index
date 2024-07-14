package ro.go.adrhc.persistence.lucene.index.operations;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import ro.go.adrhc.persistence.lucene.index.operations.count.IndexCountService;
import ro.go.adrhc.persistence.lucene.index.operations.retrieve.IndexRetrieveServiceImpl;
import ro.go.adrhc.persistence.lucene.index.operations.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.index.operations.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.index.operations.search.QueryAndValue;
import ro.go.adrhc.persistence.lucene.index.operations.search.ScoreDocAndValues;
import ro.go.adrhc.persistence.lucene.typedcore.Indexable;
import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ReadIndexOperationsImpl<T extends Indexable<ID, T>, ID>
		implements ReadIndexOperations<T, ID> {
	private final IndexCountService countService;
	private final IndexRetrieveServiceImpl<ID, T> retrieveService;
	private final IndexSearchService<T> searchService;

	@Override
	public Optional<T> findBestMatch(BestMatchingStrategy<T> bestMatchingStrategy, Query query)
			throws IOException {
		return searchService.findBestMatch(bestMatchingStrategy, query);
	}

	@Override
	public Optional<T> findBestMatch(Query query) throws IOException {
		return searchService.findBestMatch(query);
	}

	@Override
	public List<QueryAndValue<T>> findBestMatches(BestMatchingStrategy<T> bestMatchingStrategy,
			Collection<? extends Query> queries) throws IOException {
		return searchService.findBestMatches(bestMatchingStrategy, queries);
	}

	@Override
	public List<QueryAndValue<T>> findBestMatches(Collection<? extends Query> queries)
			throws IOException {
		return searchService.findBestMatches(queries);
	}

	@Override
	public List<T> findMany(Query query) throws IOException {
		return searchService.findMany(query);
	}

	@Override
	public ScoreDocAndValues<T> findMany(Query query, int hitsCount) throws IOException {
		return searchService.findMany(query, hitsCount);
	}

	@Override
	public ScoreDocAndValues<T> findMany(Query query, int hitsCount, Sort sort) throws IOException {
		return searchService.findMany(query, hitsCount, sort);
	}

	@Override
	public ScoreDocAndValues<T> findMany(Query query, Sort sort) throws IOException {
		return searchService.findMany(query, sort);
	}

	@Override
	public ScoreDocAndValues<T> findManyAfter(ScoreDoc after, Query query, int hitsCount, Sort sort)
			throws IOException {
		return searchService.findManyAfter(after, query, hitsCount, sort);
	}

	@Override
	public ScoreDocAndValues<T> findManyAfter(ScoreDoc after, Query query, Sort sort)
			throws IOException {
		return searchService.findManyAfter(after, query, sort);
	}

	@Override
	public Optional<T> findById(ID id) throws IOException {
		return retrieveService.findById(id);
	}

	@Override
	public Set<T> findByIds(Set<ID> ids) throws IOException {
		return retrieveService.findByIds(ids);
	}

	@Override
	public List<T> getAll() throws IOException {
		return retrieveService.getAll();
	}

	@Override
	public List<ID> getAllIds() throws IOException {
		return retrieveService.getAllIds();
	}

	@Override
	public <F> List<F> getFieldOfAll(LuceneFieldSpec<T> field) throws IOException {
		return retrieveService.getFieldOfAll(field);
	}

	@Override
	public <R> R reduce(Function<Stream<T>, R> reducer) throws IOException {
		return retrieveService.reduce(reducer);
	}

	@Override
	public <R> R reduceIds(Function<Stream<ID>, R> idsReducer) throws IOException {
		return retrieveService.reduceIds(idsReducer);
	}

	@Override
	public int count() throws IOException {
		return countService.count();
	}

	@Override
	public int count(Query query) throws IOException {
		return countService.count(query);
	}
}
