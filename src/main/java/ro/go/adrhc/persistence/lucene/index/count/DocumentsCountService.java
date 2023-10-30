package ro.go.adrhc.persistence.lucene.index.count;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReader;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.domain.queries.SearchedToQueryConverter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class DocumentsCountService<S> implements IndexCountService<S> {
	private final SearchedToQueryConverter<S> toQueryConverter;
	private final DocumentIndexReaderTemplate documentIndexReaderTemplate;

	/**
	 * Query base DocumentsCountService
	 * <p>
	 * constructor parameters union
	 * <p>
	 * SearchedToQueryConverter = Optional::of
	 */
	public static DocumentsCountService<Query> create(Path indexPath) {
		return create(Optional::of, indexPath);
	}

	/**
	 * constructor parameters union
	 */
	public static <S> DocumentsCountService<S> create(
			SearchedToQueryConverter<S> toQueryConverter, Path indexPath) {
		return new DocumentsCountService<>(toQueryConverter,
				DocumentIndexReaderTemplate.create(indexPath));
	}

	@Override
	public int count() throws IOException {
		return documentIndexReaderTemplate.useReader(DocumentIndexReader::count);
	}

	@Override
	public int count(S searched) throws IOException {
		Optional<Query> query = toQueryConverter.convert(searched);
		if (query.isEmpty()) {
			throw new IOException("Failed to create the lucene query!");
		}
		return documentIndexReaderTemplate.useReader(indexReader -> indexReader.count(query.get()));
	}
}
