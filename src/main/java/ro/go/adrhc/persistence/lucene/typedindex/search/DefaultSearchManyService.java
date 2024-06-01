package ro.go.adrhc.persistence.lucene.typedindex.search;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreDocAndValue;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReader;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class DefaultSearchManyService<T> implements SearchManyService<T> {
	private final TypedIndexReaderTemplate<Object, T> indexReaderTemplate;
	private final SearchResultFilter<T> searchResultFilter;

	@Override
	public List<T> findMany(Query query) throws IOException {
		return useReader(r -> filterAndMap(r.findMany(query)));
	}

	@Override
	public ScoreDocAndValues<T> findMany(Query query,
			int hitsCount, Sort sort) throws IOException {
		return useReader(r -> doFindSorted(r.findMany(query, hitsCount, sort)));
	}

	@Override
	public ScoreDocAndValues<T> findManyAfter(ScoreDoc after,
			Query query, int hitsCount, Sort sort) throws IOException {
		return useReader(r -> doFindSorted(r.findManyAfter(after, query, hitsCount, sort)));
	}

	protected ScoreDocAndValues<T> doFindSorted(Stream<ScoreDocAndValue<T>> stream) {
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
			SneakyFunction<TypedIndexReader<Object, T>, R, E> indexReaderFn)
			throws E, IOException {
		return indexReaderTemplate.useReader(indexReaderFn);
	}
}
