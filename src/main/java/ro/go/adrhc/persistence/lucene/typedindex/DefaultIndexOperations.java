package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import ro.go.adrhc.persistence.lucene.index.DocsCountService;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedAddService;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.reset.TypedResetService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedShallowUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedRetrieveService;
import ro.go.adrhc.persistence.lucene.typedindex.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.typedindex.search.DefaultIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.search.QueryAndValue;
import ro.go.adrhc.persistence.lucene.typedindex.search.ScoreDocAndValues;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedUpsertService;

import java.io.IOException;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import static ro.go.adrhc.persistence.lucene.typedcore.Identifiable.toIds;

@RequiredArgsConstructor
public class DefaultIndexOperations<ID, T
		extends Indexable<ID, T>> implements IndexOperations<ID, T> {
	private final DefaultIndexSearchService<T> searchService;
	private final TypedRetrieveService<ID, T> retrieveService;
	private final DocsCountService countService;
	private final TypedAddService<T> addService;
	private final TypedUpsertService<T> upsertService;
	private final TypedRemoveService<ID> removeService;
	private final TypedResetService<T> resetService;
	private final TypedShallowUpdateService<ID, T> shallowUpdateService;

	@Override
	public <R> R reduce(Function<Stream<T>, R> reducer) throws IOException {
		return retrieveService.reduce(reducer);
	}

	@Override
	public <R> R reduceIds(Function<Stream<ID>, R> idsReducer) throws IOException {
		return retrieveService.reduceIds(idsReducer);
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
	public <F> List<F> getFieldOfAll(TypedField<T> field) throws IOException {
		return retrieveService.getFieldOfAll(field);
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
	public ScoreDocAndValues<T> findMany(Query query, int hitsCount, Sort sort) throws IOException {
		return searchService.findMany(query, hitsCount, sort);
	}

	@Override
	public ScoreDocAndValues<T> findMany(Query query, int hitsCount) throws IOException {
		return searchService.findMany(query, hitsCount);
	}

	@Override
	public ScoreDocAndValues<T> findMany(Query query, Sort sort) throws IOException {
		return searchService.findMany(query, sort);
	}

	@Override
	public List<T> findMany(Query query) throws IOException {
		return searchService.findMany(query);
	}

	@Override
	public ScoreDocAndValues<T> findManyAfter(
			ScoreDoc after, Query query, Sort sort) throws IOException {
		return searchService.findManyAfter(after, query, sort);
	}

	@Override
	public ScoreDocAndValues<T> findManyAfter(ScoreDoc after,
			Query query, int hitsCount, Sort sort) throws IOException {
		return searchService.findManyAfter(after, query, hitsCount, sort);
	}

	@Override
	public Optional<T> findBestMatch(Query query) throws IOException {
		return searchService.findBestMatch(query);
	}

	@Override
	public Optional<T> findBestMatch(
			BestMatchingStrategy<T> bestMatchingStrategy,
			Query query) throws IOException {
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
	public int count() throws IOException {
		return countService.count();
	}

	@Override
	public int count(Query query) throws IOException {
		return countService.count(query);
	}

	@Override
	public void addOne(T t) throws IOException {
		addService.addOne(t);
	}

	@Override
	public void addMany(Collection<T> tCollection) throws IOException {
		addService.addMany(tCollection);
	}

	@Override
	public void addMany(Stream<T> tStream) throws IOException {
		addService.addMany(tStream);
	}

	@Override
	public void upsert(T t) throws IOException {
		upsertService.upsert(t);
	}

	public void merge(T t) throws IOException {
		merge(t, T::merge);
	}

	/**
	 * @param mergeStrategy 1st param is the stored value while the 2nd is @param t
	 * @param t             might be added (instead of merged) if is not stored yet
	 */
	public void merge(T t, BinaryOperator<T> mergeStrategy) throws IOException {
		Optional<T> storedOptional = retrieveService.findById(t.id());
		if (storedOptional.isEmpty()) {
			addOne(t);
		} else {
			upsert(mergeStrategy.apply(storedOptional.get(), t));
		}
	}

	/**
	 * @param mergeStrategy 1st param is the stored value while the 2nd is a tCollection element
	 * @param tCollection   might be added (instead of merged) if is not stored yet
	 */
	public void mergeMany(Collection<T> tCollection,
			BinaryOperator<T> mergeStrategy) throws IOException {
		Map<ID, T> stored = new HashMap<>();
		retrieveService.findByIds(toIds(tCollection)).forEach(t -> stored.put(t.id(), t));
		upsertMany(tCollection.stream().map(t -> merge(mergeStrategy, stored, t)).toList());
	}

	@Override
	public void upsertMany(Collection<T> tCollection) throws IOException {
		upsertService.upsertMany(tCollection);
	}

	@Override
	public void removeByIds(Collection<ID> ids) throws IOException {
		removeService.removeByIds(ids);
	}

	@Override
	public void removeById(ID id) throws IOException {
		removeService.removeById(id);
	}

	@Override
	public void removeByQuery(Query query) throws IOException {
		removeService.removeByQuery(query);
	}

	@Override
	public void reset(Iterable<T> tIterable) throws IOException {
		resetService.reset(tIterable);
	}

	@Override
	public void reset(Stream<T> tStream) throws IOException {
		resetService.reset(tStream);
	}

	@Override
	public void shallowUpdate(IndexDataSource<ID, T> dataSource) throws IOException {
		shallowUpdateService.shallowUpdate(dataSource);
	}

	@Override
	public void shallowUpdateSubset(IndexDataSource<ID, T> dataSource, Query query)
			throws IOException {
		shallowUpdateService.shallowUpdateSubset(dataSource, query);
	}

	private T merge(BinaryOperator<T> mergeStrategy, Map<ID, T> stored, T another) {
		T storedT = stored.get(another.id());
		return storedT == null ? another : mergeStrategy.apply(storedT, another);
	}
}
