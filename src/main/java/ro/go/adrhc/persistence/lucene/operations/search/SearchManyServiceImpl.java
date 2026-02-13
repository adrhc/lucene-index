package ro.go.adrhc.persistence.lucene.operations.search;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReader;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.core.bare.read.ScoreDocAndValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class SearchManyServiceImpl<T> implements SearchManyService<T> {
	private final HitsLimitedIndexReaderTemplate<Object, T> indexReaderTemplate;
	private final SearchResultFilter<T> searchResultFilter;

	@Override
	public List<T> findMany(Query query) throws IOException {
		return useReader(r -> filterAndMap(r.findMany(query)));
	}

	@Override
	public ScoreDocAndValues<T> findMany(Query query,
		int hitsCount, Sort sort) throws IOException {
		return useReader(r -> toScoreDocAndValues(r.findMany(query, hitsCount, sort)));
	}

	@Override
	public ScoreDocAndValues<T> findMany(Query query, int hitsCount) throws IOException {
		return useReader(r -> toScoreDocAndValues(r.findMany(query, hitsCount)));
	}

	@Override
	public ScoreDocAndValues<T> findMany(Query query, Sort sort) throws IOException {
		return useReader(r -> toScoreDocAndValues(r.findMany(query, sort)));
	}

	@Override
	public ScoreDocAndValues<T> findManyAfter(
		ScoreDoc after, Query query, Sort sort) throws IOException {
		return useReader(r -> toScoreDocAndValues(r.findManyAfter(after, query, sort)));
	}

	@Override
	public ScoreDocAndValues<T> findManyAfter(ScoreDoc after,
		Query query, int hitsCount, Sort sort) throws IOException {
		return useReader(r -> toScoreDocAndValues(
			r.findManyAfter(after, query, hitsCount, sort)));
	}

	protected ScoreDocAndValues<T> toScoreDocAndValues(Stream<ScoreDocAndValue<T>> stream) {
		List<ScoreDoc> scoreDocs = new ArrayList<>();
		List<T> values = new ArrayList<>();
		stream.forEach(sdv -> {
			scoreDocs.add(sdv.scoreDoc());
			values.add(sdv.value());
		});
		return new ScoreDocAndValues<>(values, scoreDocs);
	}

	protected List<T> filterAndMap(Stream<ScoreDocAndValue<T>> stream) {
		return stream
			.filter(searchResultFilter::filter)
			.map(ScoreDocAndValue::value)
			.toList();
	}

	private <R, E extends Exception> R useReader(
		SneakyFunction<HitsLimitedIndexReader<Object, T>, R, E> indexReaderFn)
		throws E, IOException {
		return indexReaderTemplate.useReader(indexReaderFn);
	}
}
