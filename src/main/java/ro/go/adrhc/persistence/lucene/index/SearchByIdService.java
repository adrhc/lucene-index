package ro.go.adrhc.persistence.lucene.index;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentsIndexReaderTemplate;
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
	private final DocumentsIndexReaderTemplate documentsIndexReaderTemplate;

	public static <ID> SearchByIdService<ID>
	create(TypedField<?> idField, Path indexPath) {
		ExactQuery exactQuery = ExactQuery.create(idField);
		return new SearchByIdService<>(exactQuery::newExactQuery,
				new DocumentsIndexReaderTemplate(1, indexPath));
	}

	public Optional<Document> findById(ID id) throws IOException {
		Query idQuery = idQueryProvider.apply(id);
		return documentsIndexReaderTemplate.findById(idQuery);
	}
}
