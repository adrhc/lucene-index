package ro.go.adrhc.persistence.lucene.typedindex.search;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreAndValue;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReader;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderTemplate;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class DefaultSearchManyService<T> implements SearchManyService<T> {
	private final TypedIndexReaderTemplate<Object, T> indexReaderTemplate;
	private final SearchResultFilter<T> searchResultFilter;

	public List<T> findMany(Query query, int hitsCount, Sort sort) throws IOException {
		return useReader(r -> filterAndMap(r.findMany(query, hitsCount, sort)));
	}

	@Override
	public List<T> findAllMatches(Query query) throws IOException {
		return useReader(r -> filterAndMap(r.findMany(query)));
	}

	protected List<T> filterAndMap(Stream<ScoreAndValue<T>> stream) {
		return stream
				.filter(searchResultFilter::filter)
				.map(ScoreAndValue::value)
				.toList();
	}

	private <R, E extends Exception> R useReader(
			SneakyFunction<TypedIndexReader<Object, T>, R, E> indexReaderFn)
			throws E, IOException {
		return indexReaderTemplate.useReader(indexReaderFn);
	}
}
