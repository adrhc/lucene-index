package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.DocsCountService;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedIndexAdderService;
import ro.go.adrhc.persistence.lucene.typedindex.create.TypedIndexInitService;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactories;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactoriesParams;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedIndexRetrieveService;
import ro.go.adrhc.persistence.lucene.typedindex.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedIndexUpdateService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class IndexRepository<ID, T extends Identifiable<?>> {
	private final TypedIndexSearchService<T> searchService;
	private final TypedIndexRetrieveService<ID, T> retrieveService;
	private final DocsCountService countService;
	private final TypedIndexAdderService<T> adderService;
	private final TypedIndexUpdateService<T> updateService;
	private final TypedIndexRemoveService<ID> removeService;
	private final TypedIndexInitService<ID, T> initService;
	private final TypedIndexRestoreService<ID, T> restoreService;
	private final IndexWriter indexWriter;

	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>> IndexRepository<ID, T>
	create(TypedIndexFactoriesParams<ID, T> params) {
		TypedIndexFactories<ID, T, E> factories = new TypedIndexFactories<>(params);
		TypedIndexSearchService<T> searchService = factories.createSearchService();
		TypedIndexRetrieveService<ID, T> retrieveService = factories.createIdSearchService();
		DocsCountService countService = factories.createCountService();
		TypedIndexAdderService<T> adderService = factories.createAdderService();
		TypedIndexUpdateService<T> updateService = factories.createUpdateService();
		TypedIndexRemoveService<ID> removeService = factories.createRemoveService();
		TypedIndexInitService<ID, T> initService = factories.createInitService();
		TypedIndexRestoreService<ID, T> restoreService = factories.createRestoreService();
		return new IndexRepository<>(searchService, retrieveService, countService,
				adderService, updateService, removeService, initService, restoreService,
				params.getIndexWriter());
	}

	public <R> R reduce(Function<Stream<T>, R> reducer) throws IOException {
		return retrieveService.reduce(reducer);
	}

	public <R> R reduceIds(Function<Stream<ID>, R> idsReducer) throws IOException {
		return retrieveService.reduceIds(idsReducer);
	}

	public List<T> getAll() throws IOException {
		return retrieveService.getAll();
	}

	public List<ID> getAllIds() throws IOException {
		return retrieveService.getAllIds();
	}

	public Optional<T> findById(ID id) throws IOException {
		return retrieveService.findById(id);
	}

	public List<T> findAllMatches(Query query) throws IOException {
		return searchService.findAllMatches(query);
	}

	public Optional<T> findBestMatch(Query query) throws IOException {
		return searchService.findBestMatch(query);
	}

	public Optional<T> findBestMatch(
			BestMatchingStrategy<T> bestMatchingStrategy,
			Query query) throws IOException {
		return searchService.findBestMatch(bestMatchingStrategy, query);
	}

	public List<TypedSearchResult<T>> findBestMatches(
			Collection<? extends Query> queries) throws IOException {
		return searchService.findBestMatches(queries);
	}

	public List<TypedSearchResult<T>> findBestMatches(
			BestMatchingStrategy<T> bestMatchingStrategy,
			Collection<? extends Query> queries) throws IOException {
		return searchService.findBestMatches(bestMatchingStrategy, queries);
	}

	public int count() throws IOException {
		return countService.count();
	}

	public int count(Query query) throws IOException {
		return countService.count(query);
	}

	public void addOne(T t) throws IOException {
		adderService.addOne(t);
		indexWriter.commit();
	}

	public void addMany(Collection<T> tCollection) throws IOException {
		adderService.addMany(tCollection);
		indexWriter.commit();
	}

	public void addMany(Stream<T> tStream) throws IOException {
		adderService.addMany(tStream);
		indexWriter.commit();
	}

	public void update(T t) throws IOException {
		updateService.update(t);
		indexWriter.commit();
	}

	public void removeByIds(Collection<ID> ids) throws IOException {
		removeService.removeByIds(ids);
		indexWriter.commit();
	}

	public void removeById(ID id) throws IOException {
		removeService.removeById(id);
		indexWriter.commit();
	}

	public void initialize(Iterable<T> tIterable) throws IOException {
		initService.initialize(tIterable);
		indexWriter.commit();
	}

	public void initialize(Stream<T> tStream) throws IOException {
		initService.initialize(tStream);
		indexWriter.commit();
	}

	public void restore(IndexDataSource<ID, T> dataSource) throws IOException {
		restoreService.restore(dataSource);
		indexWriter.commit();
	}
}
