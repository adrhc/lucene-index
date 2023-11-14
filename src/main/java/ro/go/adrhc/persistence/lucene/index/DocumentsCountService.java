package ro.go.adrhc.persistence.lucene.index;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.read.DocumentsIndexReader;
import ro.go.adrhc.persistence.lucene.core.read.DocumentsIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class DocumentsCountService implements IndexCountService {
	private final DocumentsIndexReaderTemplate documentsIndexReaderTemplate;

	/**
	 * Query base DocumentsCountService
	 * <p>
	 * constructor parameters union
	 * <p>
	 * SearchedToQueryConverter = Optional::of
	 */
	public static DocumentsCountService create(IndexReaderPool indexReaderPool) {
		return new DocumentsCountService(
				DocumentsIndexReaderTemplate.create(Integer.MAX_VALUE, indexReaderPool));
	}

	@Override
	public int count() throws IOException {
		return documentsIndexReaderTemplate.useReader(DocumentsIndexReader::count);
	}

	@Override
	public int count(Query query) throws IOException {
		return documentsIndexReaderTemplate.useReader(indexReader -> indexReader.count(query));
	}
}