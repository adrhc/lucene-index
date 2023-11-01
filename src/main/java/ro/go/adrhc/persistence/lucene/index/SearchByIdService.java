package ro.go.adrhc.persistence.lucene.index;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedindex.domain.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Slf4j
public class SearchByIdService<ID> {
	private final Function<ID, Query> idQueryProvider;
	private final DocumentIndexReaderTemplate documentIndexReaderTemplate;

	public static <ID, E extends TypedField<?>>
	SearchByIdService<ID> create(E idField, Path indexPath) {
		ExactQuery exactQuery = ExactQuery.createIdFieldQueries(idField);
		return new SearchByIdService<>(exactQuery::newExactQuery,
				new DocumentIndexReaderTemplate(1, indexPath));
	}

	public Optional<Document> findById(ID id) throws IOException {
		Query idQuery = idQueryProvider.apply(id);
		return documentIndexReaderTemplate.findById(idQuery);
	}
}
