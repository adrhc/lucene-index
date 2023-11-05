package ro.go.adrhc.persistence.lucene.index.update;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.core.write.DocumentIndexWriterTemplate;
import ro.go.adrhc.persistence.lucene.typedindex.domain.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;

import static ro.go.adrhc.persistence.lucene.index.core.write.DocumentIndexWriterTemplate.fsWriterTemplate;

@RequiredArgsConstructor
@Slf4j
public class DocumentsIndexUpdateService implements IndexUpdateService<Document> {
	private final ExactQuery exactQuery;
	private final DocumentIndexWriterTemplate indexWriterTemplate;

	/**
	 * constructor parameters union
	 */
	public static DocumentsIndexUpdateService
	create(TypedField<?> idField, Analyzer analyzer, Path indexPath) {
		ExactQuery exactQuery = ExactQuery.create(idField);
		return new DocumentsIndexUpdateService(exactQuery, fsWriterTemplate(analyzer, indexPath));
	}

	@Override
	public void add(Document document) throws IOException {
		indexWriterTemplate.useWriter(writer -> writer.addDocument(document));
	}

	@Override
	public void addAll(Collection<Document> documents) throws IOException {
		indexWriterTemplate.useWriter(writer -> writer.addDocuments(documents));
	}

	@Override
	public void addAll(Stream<Document> documents) throws IOException {
		indexWriterTemplate.useWriter(writer -> writer.addDocuments(documents));
	}

	/*@Override
	public void update(Document document) throws IOException {
		Term idTerm = new Term(idFieldName, document.getBinaryValue(idFieldName));
		indexWriterTemplate.useWriter(writer -> writer.update(idTerm, document));
	}*/

	@Override
	public void update(Document document) throws IOException {
		indexWriterTemplate.useWriter(writer -> writer
				.update(exactQuery.newExactQuery(document), document));
	}
}
