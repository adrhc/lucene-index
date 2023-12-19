package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResult;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class IndexRepositoryImpl<ID, T extends Identifiable<ID>> implements IndexRepository<ID, T> {
    protected final IndexOperations<ID, T> indexOperations;
    protected final TypedIndexContext<T> context;

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
    public Optional<T> findById(ID id) throws IOException {
        return indexOperations.findById(id);
    }

    @Override
    public Set<T> findByIds(Set<ID> ids) throws IOException {
        return indexOperations.findByIds(ids);
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
    public Optional<T> findBestMatch(BestMatchingStrategy<T> bestMatchingStrategy, Query query) throws IOException {
        return indexOperations.findBestMatch(bestMatchingStrategy, query);
    }

    @Override
    public List<TypedSearchResult<T>> findBestMatches(Collection<? extends Query> queries) throws IOException {
        return indexOperations.findBestMatches(queries);
    }

    @Override
    public List<TypedSearchResult<T>> findBestMatches(BestMatchingStrategy<T> bestMatchingStrategy,
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
        context.commit();
    }

    @Override
    public void addMany(Collection<T> tCollection) throws IOException {
        indexOperations.addMany(tCollection);
        context.commit();
    }

    @Override
    public void addMany(Stream<T> tStream) throws IOException {
        indexOperations.addMany(tStream);
        context.commit();
    }

    @Override
    public void upsert(T t) throws IOException {
        indexOperations.upsert(t);
        context.commit();
    }

    @Override
    public void removeByIds(Collection<ID> ids) throws IOException {
        indexOperations.removeByIds(ids);
        context.commit();
    }

    @Override
    public void removeById(ID id) throws IOException {
        indexOperations.removeById(id);
        context.commit();
    }

    @Override
    public void reset(Iterable<T> tIterable) throws IOException {
        indexOperations.reset(tIterable);
        context.commit();
    }

    @Override
    public void reset(Stream<T> tStream) throws IOException {
        indexOperations.reset(tStream);
        context.commit();
    }

    @Override
    public void restore(IndexDataSource<ID, T> dataSource) throws IOException {
        indexOperations.restore(dataSource);
        context.commit();
    }
}
