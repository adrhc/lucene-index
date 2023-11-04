package ro.go.adrhc.persistence.lucene.index.count;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReader;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;

import java.io.IOException;
import java.nio.file.Path;

@RequiredArgsConstructor
@Slf4j
public class DocumentsCountService implements IndexCountService {
	private final DocumentIndexReaderTemplate documentIndexReaderTemplate;

	/**
	 * Query base DocumentsCountService
	 * <p>
	 * constructor parameters union
	 * <p>
	 * SearchedToQueryConverter = Optional::of
	 */
	public static DocumentsCountService create(Path indexPath) {
		return new DocumentsCountService(DocumentIndexReaderTemplate.create(indexPath));
	}

	@Override
	public int count() throws IOException {
		return documentIndexReaderTemplate.useReader(DocumentIndexReader::count);
	}

	@Override
	public int count(Query query) throws IOException {
		return documentIndexReaderTemplate.useReader(indexReader -> indexReader.count(query));
	}
}
