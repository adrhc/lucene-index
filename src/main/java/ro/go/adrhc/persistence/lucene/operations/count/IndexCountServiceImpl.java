package ro.go.adrhc.persistence.lucene.operations.count;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexReader;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexReaderTemplateFactory;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class IndexCountServiceImpl implements IndexCountService {
	private final DocIndexReaderTemplate docsReaderTemplate;

	/**
	 * Query base IndexCountServiceImpl
	 * <p>
	 * constructor parameters union
	 * <p>
	 * SearchedToQueryConverter = Optional::of
	 */
	public static IndexCountServiceImpl create(IndexReaderPool indexReaderPool) {
		return new IndexCountServiceImpl(DocIndexReaderTemplateFactory.of(indexReaderPool));
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
		return docsReaderTemplate.useReader(indexReader -> indexReader.count(query));
	}
}
