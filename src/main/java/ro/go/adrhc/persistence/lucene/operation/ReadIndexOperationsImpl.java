package ro.go.adrhc.persistence.lucene.operation;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.springframework.lang.Nullable;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexCounter;
import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.read.IndexReadService;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.service.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.service.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.service.search.QueryAndValue;
import ro.go.adrhc.persistence.lucene.service.search.ScoreDocAndValues;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ReadIndexOperationsImpl<T extends Indexable<I, T>, I>
	implements ReadIndexOperations<T, I> {
	private final LuceneFieldSpec<T> idField;
	private final TypedIndexReaderTemplate<I, T> unlimitedIdxReaderTemplate;
	private final DocIndexCounter countService;
	private final IndexReadService<I, T> retrieveService;
	private final IndexSearchService<T> searchService;

	@Override
	public boolean hasAfter(ScoreDoc scoreDoc, Sort sort) throws IOException {
		return searchService.hasAfter(scoreDoc, sort);
	}

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
	public Optional<T> findById(I id) throws IOException {
		return retrieveService.findById(id);
	}

	@Override
	public Set<T> findByIds(Set<I> ids) throws IOException {
		return retrieveService.findByIds(ids);
	}

	@Override
	public List<T> getAll() throws IOException {
		return retrieveService.getAll();
	}

	@Override
	public List<I> findIds(Query query) throws IOException {
		return findIds(query, null);
	}

	@Override
	public List<I> findIds(Query query, @Nullable Sort sort) throws IOException {
		return unlimitedIdxReaderTemplate
			.useReader(r -> r.findIds(query, sort)
				.<I>map(idField::toPropertyValue).toList());
	}

	@Override
	public List<I> getAllIds() throws IOException {
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
	public <R> R reduceIds(Function<Stream<I>, R> idsReducer) throws IOException {
		return retrieveService.reduceIds(idsReducer);
	}

	@Override
	public <P> List<P> getFieldOfAll(LuceneFieldSpec<T> field) throws IOException {
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
