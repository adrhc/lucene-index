package ro.go.adrhc.persistence.lucene.index.update;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.core.write.DocsIndexWriterTemplate;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexContext;
import ro.go.adrhc.persistence.lucene.typedindex.domain.ExactQuery;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class DocumentsIndexUpdateService implements IndexUpdateService<Document> {
	private final ExactQuery exactQuery;
	private final DocsIndexWriterTemplate indexWriterTemplate;

	/**
	 * constructor parameters union
	 */
	public static DocumentsIndexUpdateService create(TypedIndexContext<?, ?, ?> typedIndexContext) {
		return new DocumentsIndexUpdateService(
				ExactQuery.create(typedIndexContext.getIdField()),
				new DocsIndexWriterTemplate(typedIndexContext.getIndexWriter()));
	}

	@Override
	public void add(Document document) throws IOException {
		indexWriterTemplate.useWriter(writer -> writer.addOne(document));
	}

	@Override
	public void addAll(Collection<Document> documents) throws IOException {
		indexWriterTemplate.useWriter(writer -> writer.addMany(documents));
	}

	@Override
	public void addAll(Stream<Document> documents) throws IOException {
		indexWriterTemplate.useWriter(writer -> writer.addMany(documents));
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
