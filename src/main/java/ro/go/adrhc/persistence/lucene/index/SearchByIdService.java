package ro.go.adrhc.persistence.lucene.index;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedindex.domain.IdFieldQueries;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Slf4j
public class SearchByIdService<ID> {
	private final DocumentIndexReaderTemplate documentIndexReaderTemplate;
	private final Function<ID, Query> idQueryProvider;

	public static <ID, E extends TypedField<?>>
	SearchByIdService<ID> create(E idField, Analyzer analyzer, Path indexPath) {
		IdFieldQueries idFieldQueries = IdFieldQueries.createIdFieldQueries(analyzer, idField);
		return new SearchByIdService<>(
				new DocumentIndexReaderTemplate(1, indexPath),
				idFieldQueries::newExactQuery);
	}

	public Optional<Document> findById(ID id) throws IOException {
		Query idQuery = idQueryProvider.apply(id);
		return documentIndexReaderTemplate.findById(idQuery);
	}
}
