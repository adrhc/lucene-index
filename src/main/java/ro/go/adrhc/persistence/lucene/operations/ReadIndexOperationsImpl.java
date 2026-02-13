package ro.go.adrhc.persistence.lucene.operations;

import com.rainerhahnekamp.sneakythrow.functional.SneakySupplier;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.springframework.lang.Nullable;
import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReader;
import ro.go.adrhc.persistence.lucene.operations.count.IndexCountService;
import ro.go.adrhc.persistence.lucene.operations.retrieve.IndexRetrieveService;
import ro.go.adrhc.persistence.lucene.operations.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.operations.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.operations.search.QueryAndValue;
import ro.go.adrhc.persistence.lucene.operations.search.ScoreDocAndValues;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ReadIndexOperationsImpl<T extends Indexable<ID, T>, ID>
	implements ReadIndexOperations<T, ID> {
	private final LuceneFieldSpec<T> idField;
	private final SneakySupplier<HitsLimitedIndexReader<ID, T>, IOException> unlimitedIdxReaderFactory;
	private final IndexCountService countService;
	private final IndexRetrieveService<ID, T> retrieveService;
	private final IndexSearchService<T> searchService;

	@Override
	public ScoreDocAndValues<T> findManyAfter(
		ScoreDoc after, Query query, Sort sort) throws IOException {
		return searchService.findManyAfter(after, query, sort);
	}

	@Override
	public ScoreDocAndValues<T> findManyAfter(
		ScoreDoc after, Query query, int hitsCount, Sort sort) throws IOException {
		return searchService.findManyAfter(after, query, hitsCount, sort);
	}

	@Override
	public Optional<T> findBestMatch(Query query) throws IOException {
		return searchService.findBestMatch(query);
	}

	@Override
	public Optional<T> findBestMatch(
		BestMatchingStrategy<T> bestMatchingStrategy, Query query) throws IOException {
		return searchService.findBestMatch(bestMatchingStrategy, query);
	}

	@Override
	public List<QueryAndValue<T>> findBestMatches(
		Collection<? extends Query> queries) throws IOException {
		return searchService.findBestMatches(queries);
	}

	@Override
	public List<QueryAndValue<T>> findBestMatches(
		BestMatchingStrategy<T> bestMatchingStrategy,
		Collection<? extends Query> queries) throws IOException {
		return searchService.findBestMatches(bestMatchingStrategy, queries);
	}

	@Override
	public List<T> findMany(Query query) throws IOException {
		return searchService.findMany(query);
	}

	@Override
	public ScoreDocAndValues<T> findMany(Query query, Sort sort) throws IOException {
		return searchService.findMany(query, sort);
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
	public List<ID> findIds(Query query) throws IOException {
		return findIds(query, null);
	}

	@Override
	public List<ID> findIds(Query query, @Nullable Sort sort) throws IOException {
		try (HitsLimitedIndexReader<ID, ?> reader = unlimitedIdxReaderFactory.get()) {
			return reader.findIds(query, sort).<ID>map(idField::toPropValue).toList();
		}
	}

	@Override
	public List<ID> getAllIds() throws IOException {
		return retrieveService.getAllIds();
	}

	@Override
	public void readAll(Consumer<Stream<T>> consumer) throws IOException {
		retrieveService.readAll(consumer);
	}

	@Override
	public <R> R reduceAll(Function<Stream<T>, R> reducer) throws IOException {
		return retrieveService.reduceAll(reducer);
	}

	@Override
	public <R> R reduceIds(Function<Stream<ID>, R> idsReducer) throws IOException {
		return retrieveService.reduceIds(idsReducer);
	}

	@Override
	public <F> List<F> getFieldOfAll(LuceneFieldSpec<T> field) throws IOException {
		return retrieveService.getFieldOfAll(field);
	}

	@Override
	public boolean isEmpty() throws IOException {
		return countService.isEmpty();
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
