package ro.go.adrhc.persistence.lucene.typedindex.retrieve;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.typedcore.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIdIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TypedIndexRetrieveService<ID, T> implements IndexRetrieveService<ID, T> {
	private final ExactQuery exactQuery;
	private final TypedIndexReaderTemplate<T> indexReaderTemplate;
	private final TypedIdIndexReaderTemplate<ID> typedIdIndexReaderTemplate;

	public static <ID, T> TypedIndexRetrieveService<ID, T>
	create(TypedIndexRetrieveServiceParams<T> params) {
		return new TypedIndexRetrieveService<>(
				ExactQuery.create(params.getIdField()),
				TypedIndexReaderTemplate.create(params),
				TypedIdIndexReaderTemplate.create(params));
	}

	@Override
	public <R> R reduce(Function<Stream<T>, R> reducer) throws IOException {
		return indexReaderTemplate.useReader(reader -> reducer.apply(reader.getAll()));
	}

	@Override
	public <R> R reduceIds(Function<Stream<ID>, R> idsReducer) throws IOException {
		return typedIdIndexReaderTemplate.useIdsReader(idsReader -> idsReducer.apply(idsReader.getAllIds()));
	}

	@Override
	public List<T> getAll() throws IOException {
		return indexReaderTemplate.useReader(reader -> reader.getAll().toList());
	}

	@Override
	public List<ID> getAllIds() throws IOException {
		return typedIdIndexReaderTemplate.useIdsReader(idsReader -> idsReader.getAllIds().toList());
	}

	@Override
	public Optional<T> findById(ID id) throws IOException {
		return indexReaderTemplate.useReader(reader ->
				reader.findFirst(exactQuery.newExactQuery(id)));
	}
}
