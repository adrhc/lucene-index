package ro.go.adrhc.persistence.lucene.operations.retrieve;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.BooleanQuery;
import ro.go.adrhc.persistence.lucene.core.typed.ExactQuery;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.core.typed.read.OneHitIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.core.typed.read.ScoreDocAndValue;

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
public class IndexRetrieveServiceImpl<ID, T> implements IndexRetrieveService<ID, T> {
	private final ExactQuery exactQuery;
	private final HitsLimitedIndexReaderTemplate<ID, T> indexReaderTemplate;
	private final OneHitIndexReaderTemplate<T> oneHitIndexReaderTemplate;

	public static <ID, T> IndexRetrieveServiceImpl<ID, T>
	create(IndexRetrieveServiceParams<T> params) {
		return new IndexRetrieveServiceImpl<>(
				ExactQuery.create(params.getIdField()),
				HitsLimitedIndexReaderTemplate.create(params.allHitsTypedIndexReaderParams()),
				OneHitIndexReaderTemplate.create(params));
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
	public <F> List<F> getFieldOfAll(LuceneFieldSpec<T> field) throws IOException {
		return indexReaderTemplate.useReader(reader -> reader.<F>getFieldOfAll(field).toList());
	}

	@Override
	public Optional<T> findById(ID id) throws IOException {
		return oneHitIndexReaderTemplate.useOneHitReader(r ->
				r.findFirst(exactQuery.newExactQuery(id)).map(ScoreDocAndValue::value));
	}

	@Override
	public Set<T> findByIds(Set<ID> ids) throws IOException {
		BooleanQuery idsQuery = shouldSatisfy(exactQuery.newExactQueries(ids));
		return indexReaderTemplate.useReader(reader -> reader
				.findMany(idsQuery)
				.map(ScoreDocAndValue::value)
				.collect(Collectors.toSet()));
	}
}
