package ro.go.adrhc.persistence.lucene.operations.retrieve;

import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public interface IndexRetrieveService<ID, T> {
	void readAll(Consumer<Stream<T>> consumer) throws IOException;
	
	<R> R reduceAll(Function<Stream<T>, R> reducer) throws IOException;

	<R> R reduceIds(Function<Stream<ID>, R> idsReducer) throws IOException;

	List<T> getAll() throws IOException;

	List<ID> getAllIds() throws IOException;

	<F> List<F> getFieldOfAll(LuceneFieldSpec<T> field) throws IOException;

	Optional<T> findById(ID id) throws IOException;

	Set<T> findByIds(Set<ID> ids) throws IOException;
}
