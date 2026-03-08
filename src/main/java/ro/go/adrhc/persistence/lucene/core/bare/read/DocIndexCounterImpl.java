package ro.go.adrhc.persistence.lucene.core.bare.read;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class DocIndexCounterImpl implements DocIndexCounter {
	private final DocIndexReaderTemplate docsReaderTemplate;

	/**
	 * Query base DocIndexCounterImpl
	 * <p>
	 * constructor parameters union
	 * <p>
	 * SearchedToQueryConverter = Optional::of
	 */
	public static DocIndexCounterImpl create(IndexReaderPool indexReaderPool) {
		return new DocIndexCounterImpl(DocIndexReaderTemplateFactory.of(indexReaderPool));
	}

	public static DocIndexCounterImpl create(DocIndexReaderParams params) {
		return new DocIndexCounterImpl(DocIndexReaderTemplateFactory.of(params));
	}

	@Override
	public boolean isEmpty() throws IOException {
		return docsReaderTemplate.useReader(DocIndexReader::isEmpty);
	}

	@Override
	public int count() throws IOException {
		return docsReaderTemplate.useReader(DocIndexReader::count);
	}

	@Override
	public int count(Query query) throws IOException {
		return docsReaderTemplate.useReader(indexReader -> indexReader.countByQuery(query));
	}
}
