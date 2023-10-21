package ro.go.adrhc.persistence.lucene.index;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.core.write.DocumentIndexWriterTemplate;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
public class LuceneIndex {
	private final String idFieldName;
	private final DocumentIndexWriterTemplate indexWriterTemplate;

	public void addDocument(Document document) throws IOException {
		indexWriterTemplate.useWriter(writer -> writer.addDocument(document));
	}

	public void addDocuments(Collection<Document> documents) throws IOException {
		indexWriterTemplate.useWriter(writer -> writer.addDocuments(documents));
	}

	public void removeByIds(Collection<String> ids) throws IOException {
		indexWriterTemplate.useWriter(writer -> writer.removeByFieldValues(idFieldName, ids));
	}
}
