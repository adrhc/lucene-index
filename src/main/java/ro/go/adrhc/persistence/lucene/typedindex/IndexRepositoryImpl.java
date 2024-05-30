package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.typedindex.search.QueryAndValue;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexParams;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class IndexRepositoryImpl<ID, T extends Indexable<ID, T>>
		implements IndexRepository<ID, T> {
	protected final IndexOperations<ID, T> indexOperations;
	@Getter
	protected final TypedIndexParams<T> typedIndexParams;

	@Override
	public <R> R reduce(Function<Stream<T>, R> reducer) throws IOException {
		return indexOperations.reduce(reducer);
	}

	@Override
	public <R> R reduceIds(Function<Stream<ID>, R> idsReducer) throws IOException {
		return indexOperations.reduceIds(idsReducer);
	}

	@Override
	public List<T> getAll() throws IOException {
		return indexOperations.getAll();
	}

	@Override
	public List<ID> getAllIds() throws IOException {
		return indexOperations.getAllIds();
	}

	@Override
	public <F> List<F> getFieldOfAll(TypedField<T> field) throws IOException {
		return indexOperations.getFieldOfAll(field);
	}

	@Override
	public Optional<T> findById(ID id) throws IOException {
		return indexOperations.findById(id);
	}

	@Override
	public Set<T> findByIds(Set<ID> ids) throws IOException {
		return indexOperations.findByIds(ids);
	}

	@Override
	public List<T> findMany(Query query, int hitsCount, Sort sort) throws IOException {
		return indexOperations.findMany(query, hitsCount, sort);
	}

	@Override
	public List<T> findAllMatches(Query query) throws IOException {
		return indexOperations.findAllMatches(query);
	}

	@Override
	public Optional<T> findBestMatch(Query query) throws IOException {
		return indexOperations.findBestMatch(query);
	}

	@Override
	public Optional<T> findBestMatch(BestMatchingStrategy<T> bestMatchingStrategy, Query query)
			throws IOException {
		return indexOperations.findBestMatch(bestMatchingStrategy, query);
	}

	@Override
	public List<QueryAndValue<T>> findBestMatches(Collection<? extends Query> queries)
			throws IOException {
		return indexOperations.findBestMatches(queries);
	}

	@Override
	public List<QueryAndValue<T>> findBestMatches(BestMatchingStrategy<T> bestMatchingStrategy,
			Collection<? extends Query> queries) throws IOException {
		return indexOperations.findBestMatches(bestMatchingStrategy, queries);
	}

	@Override
	public int count() throws IOException {
		return indexOperations.count();
	}

	@Override
	public int count(Query query) throws IOException {
		return indexOperations.count(query);
	}

	@Override
	public void addOne(T t) throws IOException {
		indexOperations.addOne(t);
		commit();
	}

	@Override
	public void addMany(Collection<T> tCollection) throws IOException {
		indexOperations.addMany(tCollection);
		commit();
	}

	@Override
	public void addMany(Stream<T> tStream) throws IOException {
		indexOperations.addMany(tStream);
		commit();
	}

	@Override
	public void upsert(T t) throws IOException {
		indexOperations.upsert(t);
		commit();
	}

	@Override
	public void merge(T t) throws IOException {
		indexOperations.merge(t);
		commit();
	}

	@Override
	public void upsertAll(Iterable<T> iterable) throws IOException {
		indexOperations.upsertAll(iterable);
		commit();
	}

	@Override
	public void removeByIds(Collection<ID> ids) throws IOException {
		indexOperations.removeByIds(ids);
		commit();
	}

	@Override
	public void removeById(ID id) throws IOException {
		indexOperations.removeById(id);
		commit();
	}

	@Override
	public void removeByQuery(Query query) throws IOException {
		indexOperations.removeByQuery(query);
		commit();
	}

	@Override
	public void reset(Iterable<T> tIterable) throws IOException {
		indexOperations.reset(tIterable);
		commit();
	}

	@Override
	public void reset(Stream<T> tStream) throws IOException {
		indexOperations.reset(tStream);
		commit();
	}

	@Override
	public void restore(IndexDataSource<ID, T> dataSource) throws IOException {
		indexOperations.restore(dataSource);
		commit();
	}

	@Override
	public void restoreSubset(IndexDataSource<ID, T> dataSource, Query query) throws IOException {
		indexOperations.restoreSubset(dataSource, query);
		commit();
	}

	@Override
	public void close() throws IOException {
		typedIndexParams.close();
	}

	protected void commit() throws IOException {
		if (typedIndexParams.isReadOnly()) {
			throw new UnsupportedOperationException("Can't modify, the index is read-only!");
		}
		typedIndexParams.getIndexWriter().commit();
	}
}
