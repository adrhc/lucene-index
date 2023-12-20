package ro.go.adrhc.persistence.lucene.typedindex;

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

public interface IndexOperations<ID, T extends Identifiable<ID>> {
    <R> R reduce(Function<Stream<T>, R> reducer) throws IOException;

    <R> R reduceIds(Function<Stream<ID>, R> idsReducer) throws IOException;

    List<T> getAll() throws IOException;

    List<ID> getAllIds() throws IOException;

    Optional<T> findById(ID id) throws IOException;

    Set<T> findByIds(Set<ID> ids) throws IOException;

    List<T> findAllMatches(Query query) throws IOException;

    Optional<T> findBestMatch(Query query) throws IOException;

    Optional<T> findBestMatch(
            BestMatchingStrategy<T> bestMatchingStrategy,
            Query query) throws IOException;

    List<TypedSearchResult<T>> findBestMatches(
            Collection<? extends Query> queries) throws IOException;

    List<TypedSearchResult<T>> findBestMatches(
            BestMatchingStrategy<T> bestMatchingStrategy,
            Collection<? extends Query> queries) throws IOException;

    int count() throws IOException;

    int count(Query query) throws IOException;

    void addOne(T t) throws IOException;

    void addMany(Collection<T> tCollection) throws IOException;

    void addMany(Stream<T> tStream) throws IOException;

    void upsert(T t) throws IOException;

    void removeByIds(Collection<ID> ids) throws IOException;

    void removeById(ID id) throws IOException;

    void removeByQuery(Query query) throws IOException;

    void reset(Iterable<T> tIterable) throws IOException;

    void reset(Stream<T> tStream) throws IOException;

    void restore(IndexDataSource<ID, T> dataSource) throws IOException;
}
