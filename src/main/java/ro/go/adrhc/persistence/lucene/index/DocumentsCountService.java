package ro.go.adrhc.persistence.lucene.index;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.read.DocsIndexReader;
import ro.go.adrhc.persistence.lucene.core.read.DocsIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class DocumentsCountService implements IndexCountService {
	private final DocsIndexReaderTemplate docsReaderTemplate;

	/**
	 * Query base DocumentsCountService
	 * <p>
	 * constructor parameters union
	 * <p>
	 * SearchedToQueryConverter = Optional::of
	 */
	public static DocumentsCountService create(IndexReaderPool indexReaderPool) {
		return new DocumentsCountService(
				DocsIndexReaderTemplate.create(Integer.MAX_VALUE, indexReaderPool));
	}

	@Override
	public int count() throws IOException {
		return docsReaderTemplate.useReader(DocsIndexReader::count);
	}

	@Override
	public int count(Query query) throws IOException {
		return docsReaderTemplate.useReader(indexReader -> indexReader.count(query));
	}
}
