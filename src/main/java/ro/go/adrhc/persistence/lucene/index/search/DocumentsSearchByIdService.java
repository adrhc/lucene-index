package ro.go.adrhc.persistence.lucene.index.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.read.DocumentsIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedindex.domain.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Slf4j
public class DocumentsSearchByIdService<ID> implements SearchByIdService<ID, Document> {
	private final Function<ID, Query> idQueryProvider;
	private final DocumentsIndexReaderTemplate documentsIndexReaderTemplate;

	public static <ID> DocumentsSearchByIdService<ID>
	create(TypedField<?> idField, IndexReaderPool indexReaderPool) {
		ExactQuery exactQuery = ExactQuery.create(idField);
		return new DocumentsSearchByIdService<>(exactQuery::newExactQuery,
				new DocumentsIndexReaderTemplate(1, indexReaderPool));
	}

	@Override
	public Optional<Document> findById(ID id) throws IOException {
		Query idQuery = idQueryProvider.apply(id);
		return documentsIndexReaderTemplate.findById(idQuery);
	}
}
