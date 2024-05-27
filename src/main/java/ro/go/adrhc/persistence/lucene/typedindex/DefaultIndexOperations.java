package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.DocsCountService;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedIndexAdderService;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.reset.TypedIndexResetService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedIndexRetrieveService;
import ro.go.adrhc.persistence.lucene.typedindex.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedIndexUpsertService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DefaultIndexOperations<ID, T extends Identifiable<ID>> implements IndexOperations<ID, T> {
	private final TypedIndexSearchService<T> searchService;
	private final TypedIndexRetrieveService<ID, T> retrieveService;
	private final DocsCountService countService;
	private final TypedIndexAdderService<T> adderService;
	private final TypedIndexUpsertService<T> upsertService;
	private final TypedIndexRemoveService<ID> removeService;
	private final TypedIndexResetService<T> resetService;
	private final TypedIndexRestoreService<ID, T> restoreService;

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
	public Optional<T> findById(ID id) throws IOException {
		return retrieveService.findById(id);
	}

	@Override
	public Set<T> findByIds(Set<ID> ids) throws IOException {
		return retrieveService.findByIds(ids);
	}

	@Override
	public List<T> findAllMatches(Query query) throws IOException {
		return searchService.findAllMatches(query);
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
	public List<TypedSearchResult<T>> findBestMatches(
			Collection<? extends Query> queries) throws IOException {
		return searchService.findBestMatches(queries);
	}

	@Override
	public List<TypedSearchResult<T>> findBestMatches(
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
		adderService.addOne(t);
	}

	@Override
	public void addMany(Collection<T> tCollection) throws IOException {
		adderService.addMany(tCollection);
	}

	@Override
	public void addMany(Stream<T> tStream) throws IOException {
		adderService.addMany(tStream);
	}

	@Override
	public void upsert(T t) throws IOException {
		upsertService.upsert(t);
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
	public void restore(IndexDataSource<ID, T> dataSource) throws IOException {
		restoreService.restore(dataSource);
	}
}
