package ro.go.adrhc.persistence.lucene.typedindex.retrieve;

import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public interface IndexRetrieveService<ID, T> {
    <R> R reduce(Function<Stream<T>, R> reducer) throws IOException;

    <R> R reduceIds(Function<Stream<ID>, R> idsReducer) throws IOException;

    List<T> getAll() throws IOException;

    List<ID> getAllIds() throws IOException;

    <F> List<F> getFieldOfAll(TypedField<T> field) throws IOException;

    Optional<T> findById(ID id) throws IOException;
}
