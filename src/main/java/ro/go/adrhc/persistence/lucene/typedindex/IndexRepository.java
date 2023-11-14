package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.DocumentsCountService;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreAndTyped;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedIndexAdderService;
import ro.go.adrhc.persistence.lucene.typedindex.create.TypedIndexInitService;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactories;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactoriesParams;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.typedindex.search.CriterionScoreAndTyped;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedIndexUpdateService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class IndexRepository<ID, T extends Identifiable<?>> {
	private final TypedIndexSearchService<T> searchService;
	private final TypedSearchByIdService<ID, T> searchByIdService;
	private final DocumentsCountService countService;
	private final TypedIndexAdderService<T> adderService;
	private final TypedIndexUpdateService<T> updateService;
	private final TypedIndexRemoveService<ID> removeService;
	private final TypedIndexInitService<ID, T> initService;
	private final TypedIndexRestoreService<ID, T> restoreService;

	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>> IndexRepository<ID, T>
	create(TypedIndexFactoriesParams<ID, T, E> params) {
		TypedIndexFactories<ID, T, E> factories = new TypedIndexFactories<>(params);
		TypedIndexSearchService<T> searchService = factories.createSearchService();
		TypedSearchByIdService<ID, T> searchByIdService = factories.createSearchByIdService();
		DocumentsCountService countService = factories.createCountService();
		TypedIndexAdderService<T> adderService = factories.createAdderService();
		TypedIndexUpdateService<T> updateService = factories.createUpdateService();
		TypedIndexRemoveService<ID> removeService = factories.createRemoveService();
		TypedIndexInitService<ID, T> initService = factories.createInitService();
		TypedIndexRestoreService<ID, T> restoreService = factories.createRestoreService();
		return new IndexRepository<>(searchService, searchByIdService, countService,
				adderService, updateService, removeService, initService, restoreService);
	}

	public List<T> getAll() throws IOException {
		return searchService.getAll();
	}

	public List<T> findAllMatches(Query query) throws IOException {
		return searchService.findAllMatches(query);
	}

	public Optional<T> findBestMatch(Query query) throws IOException {
		return searchService.findBestMatch(query);
	}

	public Optional<T> findBestMatch(
			BestMatchingStrategy<ScoreAndTyped<T>> bestMatchingStrategy,
			Query query) throws IOException {
		return searchService.findBestMatch(bestMatchingStrategy, query);
	}

	public List<CriterionScoreAndTyped<Query, T>> findBestMatches(
			Collection<? extends Query> queries) throws IOException {
		return searchService.findBestMatches(queries);
	}

	public List<CriterionScoreAndTyped<Query, T>> findBestMatches(
			BestMatchingStrategy<ScoreAndTyped<T>> bestMatchingStrategy,
			Collection<? extends Query> queries) throws IOException {
		return searchService.findBestMatches(bestMatchingStrategy, queries);
	}

	public Optional<T> findById(ID id) throws IOException {
		return searchByIdService.findById(id);
	}

	public int count() throws IOException {
		return countService.count();
	}

	public int count(Query query) throws IOException {
		return countService.count(query);
	}

	public void addOne(T t) throws IOException {
		adderService.addOne(t);
	}

	public void addMany(Collection<T> tCollection) throws IOException {
		adderService.addMany(tCollection);
	}

	public void addMany(Stream<T> tStream) throws IOException {
		adderService.addMany(tStream);
	}

	public void update(T t) throws IOException {
		updateService.update(t);
	}

	public void removeByIds(Collection<ID> ids) throws IOException {
		removeService.removeByIds(ids);
	}

	public void removeById(ID id) throws IOException {
		removeService.removeById(id);
	}

	public void initialize(Iterable<T> tIterable) throws IOException {
		initService.initialize(tIterable);
	}

	public void initialize(Stream<T> tStream) throws IOException {
		initService.initialize(tStream);
	}

	public void restore(IndexDataSource<ID, T> dataSource) throws IOException {
		restoreService.restore(dataSource);
	}
}
