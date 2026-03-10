package ro.go.adrhc.persistence.lucene.core.typed.read;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.BooleanQuery;
import ro.go.adrhc.persistence.lucene.core.typed.ExactQuery;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ro.go.adrhc.persistence.lucene.core.bare.query.BooleanQueryFactory.shouldSatisfy;

@RequiredArgsConstructor
public class IndexReadServiceImpl<I, T> implements IndexReadService<I, T> {
	private final ExactQuery exactQuery;
	private final TypedIndexReaderTemplate<I, T> indexReaderTemplate;

	public static <I, T> IndexReadServiceImpl<I, T>
	create(TypedIndexReaderParams<T> params) {
		return new IndexReadServiceImpl<>(
			ExactQuery.create(params.idField()),
			TypedIndexReaderTemplate.create(params));
	}

	@Override
	public void readAll(Consumer<Stream<T>> consumer) throws IOException {
		indexReaderTemplate.withReader(reader -> consumer.accept(reader.getAll()));
	}

	@Override
	public <R> R reduceAll(Function<Stream<T>, R> reducer) throws IOException {
		return indexReaderTemplate.useReader(reader -> reducer.apply(reader.getAll()));
	}

	@Override
	public <R> R reduceIds(Function<Stream<I>, R> idsReducer) throws IOException {
		return indexReaderTemplate.useReader(reader -> idsReducer.apply(reader.getAllIds()));
	}

	@Override
	public List<T> getAll() throws IOException {
		return indexReaderTemplate.useReader(reader -> reader.getAll().toList());
	}

	@Override
	public List<I> getAllIds() throws IOException {
		return indexReaderTemplate.useReader(reader -> reader.getAllIds().toList());
	}

	/**
	 * The caller must use the proper type!
	 */
	@Override
	public <P> List<P> getFieldOfAll(LuceneFieldSpec<T> field) throws IOException {
		return indexReaderTemplate.useReader(reader -> reader.<P>getFieldValues(field).toList());
	}

	@Override
	public Optional<T> findById(I id) throws IOException {
		return indexReaderTemplate.useReader(r ->
			r.findMany(exactQuery.newExactQuery(id), 1)
				.map(ScoreAndValue::value).findFirst());
	}

	@Override
	public Set<T> findByIds(Set<I> ids) throws IOException {
		BooleanQuery idsQuery = shouldSatisfy(exactQuery.newExactQueries(ids));
		return indexReaderTemplate.useReader(reader -> reader
			.findMany(idsQuery)
			.map(ScoreAndValue::value)
			.collect(Collectors.toSet()));
	}
}
