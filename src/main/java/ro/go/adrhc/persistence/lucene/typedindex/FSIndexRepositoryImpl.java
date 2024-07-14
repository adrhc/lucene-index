package ro.go.adrhc.persistence.lucene.typedindex;

import com.rainerhahnekamp.sneakythrow.functional.SneakyRunnable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import ro.go.adrhc.persistence.lucene.typedcore.Indexable;
import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.typedindex.search.QueryAndValue;
import ro.go.adrhc.persistence.lucene.typedindex.search.ScoreDocAndValues;
import ro.go.adrhc.persistence.lucene.typedindex.srvparams.IndexServicesParamsFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class FSIndexRepositoryImpl<ID, T extends Indexable<ID, T>>
		implements FSIndexRepository<ID, T> {
	@Getter
	protected final IndexServicesParamsFactory<T> indexServicesParamsFactory;
	protected final ReadIndexOperations<T, ID> readIndexOperations;
	protected final WriteIndexOperations<T, ID> writeIndexOperations;

	public static <ID, T extends Indexable<ID, T>>
	FSIndexRepository<ID, T> of(IndexServicesParamsFactory<T> params) {
		IndexOperationsFactory<T, ID> factory = IndexOperationsFactory.of(params);
		ReadIndexOperations<T, ID> readIndexOperations = factory.createReadIndexOperations();
		WriteIndexOperations<T, ID> writeIndexOperations = factory.createWriteIndexOperations();
		return new FSIndexRepositoryImpl<>(params, readIndexOperations, writeIndexOperations);
	}

	@Override
	public int count() throws IOException {
		return readIndexOperations.count();
	}

	@Override
	public int count(Query query) throws IOException {
		return readIndexOperations.count(query);
	}

	@Override
	public Optional<T> findBestMatch(BestMatchingStrategy<T> bestMatchingStrategy, Query query)
			throws IOException {
		return readIndexOperations.findBestMatch(bestMatchingStrategy, query);
	}

	@Override
	public Optional<T> findBestMatch(Query query) throws IOException {
		return readIndexOperations.findBestMatch(query);
	}

	@Override
	public List<QueryAndValue<T>> findBestMatches(BestMatchingStrategy<T> bestMatchingStrategy,
			Collection<? extends Query> queries) throws IOException {
		return readIndexOperations.findBestMatches(bestMatchingStrategy, queries);
	}

	@Override
	public List<QueryAndValue<T>> findBestMatches(Collection<? extends Query> queries)
			throws IOException {
		return readIndexOperations.findBestMatches(queries);
	}

	@Override
	public Optional<T> findById(ID id) throws IOException {
		return readIndexOperations.findById(id);
	}

	@Override
	public Set<T> findByIds(Set<ID> ids) throws IOException {
		return readIndexOperations.findByIds(ids);
	}

	@Override
	public List<T> findMany(Query query) throws IOException {
		return readIndexOperations.findMany(query);
	}

	@Override
	public List<T> getAll() throws IOException {
		return readIndexOperations.getAll();
	}

	@Override
	public List<ID> getAllIds() throws IOException {
		return readIndexOperations.getAllIds();
	}

	@Override
	public <R> R reduce(Function<Stream<T>, R> reducer) throws IOException {
		return readIndexOperations.reduce(reducer);
	}

	@Override
	public <R> R reduceIds(Function<Stream<ID>, R> idsReducer) throws IOException {
		return readIndexOperations.reduceIds(idsReducer);
	}

	@Override
	public ScoreDocAndValues<T> findMany(Query query, int hitsCount, Sort sort) throws IOException {
		return readIndexOperations.findMany(query, hitsCount, sort);
	}

	@Override
	public ScoreDocAndValues<T> findMany(Query query, int numHits) throws IOException {
		return readIndexOperations.findMany(query, numHits);
	}

	@Override
	public ScoreDocAndValues<T> findMany(Query query, Sort sort) throws IOException {
		return readIndexOperations.findMany(query, sort);
	}

	@Override
	public ScoreDocAndValues<T> findManyAfter(ScoreDoc after, Query query, int hitsCount, Sort sort)
			throws IOException {
		return readIndexOperations.findManyAfter(after, query, hitsCount, sort);
	}

	@Override
	public ScoreDocAndValues<T> findManyAfter(ScoreDoc after, Query query, Sort sort)
			throws IOException {
		return readIndexOperations.findManyAfter(after, query, sort);
	}

	@Override
	public <F> List<F> getFieldOfAll(LuceneFieldSpec<T> field) throws IOException {
		return readIndexOperations.getFieldOfAll(field);
	}

	@Override
	public void addMany(Collection<T> tCollection) throws IOException {
		executeWrite(() -> writeIndexOperations.addMany(tCollection));
	}

	@Override
	public void addMany(Stream<T> stream) throws IOException {
		executeWrite(() -> writeIndexOperations.addMany(stream));
	}

	@Override
	public void addOne(T t) throws IOException {
		executeWrite(() -> writeIndexOperations.addOne(t));
	}

	@Override
	public void upsert(T t) throws IOException {
		executeWrite(() -> writeIndexOperations.upsert(t));
	}

	@Override
	public void upsertMany(Collection<T> tCollection) throws IOException {
		executeWrite(() -> writeIndexOperations.upsertMany(tCollection));
	}

	@Override
	public void removeById(ID id) throws IOException {
		executeWrite(() -> writeIndexOperations.removeById(id));
	}

	@Override
	public void removeByIds(Collection<ID> ids) throws IOException {
		executeWrite(() -> writeIndexOperations.removeByIds(ids));
	}

	@Override
	public void removeByQuery(Query query) throws IOException {
		executeWrite(() -> writeIndexOperations.removeByQuery(query));
	}

	@Override
	public void shallowUpdate(IndexDataSource<ID, T> dataSource) throws IOException {
		executeWrite(() -> writeIndexOperations.shallowUpdate(dataSource));
	}

	@Override
	public void shallowUpdateSubset(IndexDataSource<ID, T> dataSource, Query query)
			throws IOException {
		executeWrite(() -> writeIndexOperations.shallowUpdateSubset(dataSource, query));
	}

	@Override
	public void reset(Iterable<T> stateAfterReset) throws IOException {
		executeWrite(() -> writeIndexOperations.reset(stateAfterReset));
	}

	@Override
	public void reset(Stream<T> stateAfterReset) throws IOException {
		executeWrite(() -> writeIndexOperations.reset(stateAfterReset));
	}

	@Override
	public void merge(T t) throws IOException {
		executeWrite(() -> writeIndexOperations.merge(t));
	}

	@Override
	public void merge(T t, BinaryOperator<T> mergeStrategy) throws IOException {
		executeWrite(() -> writeIndexOperations.merge(t, mergeStrategy));
	}

	@Override
	public void mergeMany(Collection<T> tCollection, BinaryOperator<T> mergeStrategy)
			throws IOException {
		executeWrite(() -> writeIndexOperations.mergeMany(tCollection, mergeStrategy));
	}

	@Override
	public void close() throws IOException {
		executeWrite(indexServicesParamsFactory::close);
	}

	protected void executeWrite(SneakyRunnable<IOException> action) throws IOException {
		if (indexServicesParamsFactory.isReadOnly()) {
			throw new UnsupportedOperationException("Can't modify, the index is read-only!");
		}
		action.run();
		indexServicesParamsFactory.getIndexWriter().commit();
	}
}
