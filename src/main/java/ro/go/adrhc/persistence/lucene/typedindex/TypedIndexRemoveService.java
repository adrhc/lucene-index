package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.IndexRemoveService;
import ro.go.adrhc.persistence.lucene.index.core.write.DocumentsIndexWriterTemplate;
import ro.go.adrhc.persistence.lucene.typedindex.domain.ExactQuery;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class TypedIndexRemoveService<ID> implements IndexRemoveService<ID> {
	private final ExactQuery exactQuery;
	private final DocumentsIndexWriterTemplate indexWriterTemplate;

	/**
	 * constructor parameters union
	 */
	public static <ID> TypedIndexRemoveService<ID>
	create(TypedIndexSpec<ID, ?, ?> typedIndexSpec) {
		ExactQuery exactQuery = ExactQuery.create(typedIndexSpec.getIdField());
		return new TypedIndexRemoveService<>(exactQuery,
				new DocumentsIndexWriterTemplate(typedIndexSpec.getIndexWriter()));
	}

	@Override
	public void removeByIds(Collection<ID> ids) throws IOException {
		indexWriterTemplate.useWriter(writer -> writer
				.deleteDocuments(exactQuery.newExactQueries(ids)));
	}

	@Override
	public void removeById(ID id) throws IOException {
		indexWriterTemplate.useWriter(writer -> writer
				.deleteDocument(exactQuery.newExactQuery(id)));
	}
}
