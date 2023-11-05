package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import ro.go.adrhc.persistence.lucene.index.IndexRemoveService;
import ro.go.adrhc.persistence.lucene.index.core.write.DocumentIndexWriterTemplate;
import ro.go.adrhc.persistence.lucene.typedindex.domain.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import static ro.go.adrhc.persistence.lucene.index.core.write.DocumentIndexWriterTemplate.fsWriterTemplate;

@RequiredArgsConstructor
public class TypedIndexRemoveService<ID> implements IndexRemoveService<ID> {
	private final ExactQuery exactQuery;
	private final DocumentIndexWriterTemplate indexWriterTemplate;

	/**
	 * constructor parameters union
	 */
	public static <ID> TypedIndexRemoveService<ID>
	create(Analyzer analyzer, TypedField<?> idField, Path indexPath) {
		ExactQuery exactQuery = ExactQuery.create(idField);
		return new TypedIndexRemoveService<>(exactQuery, fsWriterTemplate(analyzer, indexPath));
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
