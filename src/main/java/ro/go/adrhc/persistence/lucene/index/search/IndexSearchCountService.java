package ro.go.adrhc.persistence.lucene.index.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReader;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class IndexSearchCountService<S> {
	private final SearchedToQueryConverter<S> toQueryConverter;
	private final DocumentIndexReaderTemplate documentIndexReaderTemplate;

	public static <S> IndexSearchCountService<S> create(
			SearchedToQueryConverter<S> toQueryConverter, Path indexPath) {
		return new IndexSearchCountService<>(toQueryConverter,
				DocumentIndexReaderTemplate.create(indexPath));
	}

	public int countAll() throws IOException {
		return documentIndexReaderTemplate.useReader(DocumentIndexReader::countAll);
	}

	public int count(S searched) throws IOException {
		Optional<Query> query = toQueryConverter.convert(searched);
		if (query.isEmpty()) {
			throw new IOException("Failed to create the lucene query!");
		}
		return documentIndexReaderTemplate.useReader(indexReader -> indexReader.count(query.get()));
	}
}
