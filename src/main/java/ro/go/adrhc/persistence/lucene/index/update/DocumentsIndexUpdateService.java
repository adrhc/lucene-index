package ro.go.adrhc.persistence.lucene.index.update;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.core.write.DocumentIndexWriterTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;

import static ro.go.adrhc.persistence.lucene.index.core.write.DocumentIndexWriterTemplate.fsWriterTemplate;

@RequiredArgsConstructor
@Slf4j
public class DocumentsIndexUpdateService implements IndexUpdateService<String, Document> {
	private final String idFieldName;
	private final DocumentIndexWriterTemplate indexWriterTemplate;

	/**
	 * constructor parameters union
	 */
	public static DocumentsIndexUpdateService create(
			Enum<?> idField, Analyzer analyzer, Path indexPath) {
		return create(idField, fsWriterTemplate(analyzer, indexPath));
	}

	/**
	 * ergonomic constructor parameters
	 */
	public static DocumentsIndexUpdateService create(
			Enum<?> idField, DocumentIndexWriterTemplate indexWriterTemplate) {
		return new DocumentsIndexUpdateService(idField.name(), indexWriterTemplate);
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

	@Override
	public void removeByIds(Collection<String> ids) throws IOException {
		indexWriterTemplate.useWriter(writer -> writer.removeByFieldValues(idFieldName, ids));
	}
}
