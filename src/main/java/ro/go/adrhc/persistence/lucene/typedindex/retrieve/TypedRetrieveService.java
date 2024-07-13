package ro.go.adrhc.persistence.lucene.typedindex.retrieve;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.BooleanQuery;
import ro.go.adrhc.persistence.lucene.typedcore.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.typedcore.read.OneHitIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreDocAndValue;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ro.go.adrhc.persistence.lucene.core.query.BooleanQueryFactory.shouldSatisfy;

@RequiredArgsConstructor
public class TypedRetrieveService<ID, T> implements IndexRetrieveService<ID, T> {
	private final ExactQuery exactQuery;
	private final TypedIndexReaderTemplate<ID, T> indexReaderTemplate;
	private final OneHitIndexReaderTemplate<T> oneHitIndexReaderTemplate;

	public static <ID, T> TypedRetrieveService<ID, T>
	create(TypedRetrieveServiceParams<T> params) {
		return new TypedRetrieveService<>(
				ExactQuery.create(params.getIdField()),
				TypedIndexReaderTemplate.create(params.toAllHitsTypedIndexReaderParams()),
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
