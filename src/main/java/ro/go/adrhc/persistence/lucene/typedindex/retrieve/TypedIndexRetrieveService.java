package ro.go.adrhc.persistence.lucene.typedindex.retrieve;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.typedcore.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.read.OneHitIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TypedIndexRetrieveService<ID, T> implements IndexRetrieveService<ID, T> {
	private final ExactQuery exactQuery;
	private final TypedIndexReaderTemplate<ID, T> indexReaderTemplate;
	private final OneHitIndexReaderTemplate<T> oneHitIndexReaderTemplate;

	public static <ID, T> TypedIndexRetrieveService<ID, T>
	create(TypedIndexRetrieveServiceParams<T> params) {
		return new TypedIndexRetrieveService<>(
				ExactQuery.create(params.getIdField()),
				TypedIndexReaderTemplate.create(params),
				OneHitIndexReaderTemplate.create(params));
	}

	@Override
	public <R> R reduce(Function<Stream<T>, R> reducer) throws IOException {
		return indexReaderTemplate.useReader(reader -> reducer.apply(reader.getAll()));
	}

	@Override
	public <R> R reduceIds(Function<Stream<ID>, R> idsReducer) throws IOException {
		return indexReaderTemplate.useReader(reader -> idsReducer.apply(reader.getAllIds()));
	}

	@Override
	public List<T> getAll() throws IOException {
		return indexReaderTemplate.useReader(reader -> reader.getAll().toList());
	}

	@Override
	public List<ID> getAllIds() throws IOException {
		return indexReaderTemplate.useReader(reader -> reader.getAllIds().toList());
	}

	/**
	 * The caller must use the proper type!
	 */
	@Override
	public <F> List<F> getFieldOfAll(TypedField<T> field) throws IOException {
		return indexReaderTemplate.useReader(reader -> reader.<F>getFieldOfAll(field).toList());
	}

	@Override
	public Optional<T> findById(ID id) throws IOException {
		return oneHitIndexReaderTemplate.useOneHitReader(reader ->
				reader.findFirst(exactQuery.newExactQuery(id)));
	}
}
