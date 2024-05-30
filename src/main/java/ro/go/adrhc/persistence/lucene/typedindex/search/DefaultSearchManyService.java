package ro.go.adrhc.persistence.lucene.typedindex.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreAndValue;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReader;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderTemplate;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class DefaultSearchManyService<T> implements SearchManyService<T> {
	private final TypedIndexReaderTemplate<?, T> indexReaderTemplate;
	private final SearchResultFilter<T> searchResultFilter;

	@Override
	public List<T> findAllMatches(Query query) throws IOException {
		return indexReaderTemplate.useReader(reader -> doFindAllMatches(query, reader));
	}

	protected List<T> doFindAllMatches(
			Query query, TypedIndexReader<?, T> reader) throws IOException {
		// log.debug("\nQuery used to search:\n{}", query);
		return reader.findMany(query)
				.filter(searchResultFilter::filter)
				.map(ScoreAndValue::value).toList();
	}
}
