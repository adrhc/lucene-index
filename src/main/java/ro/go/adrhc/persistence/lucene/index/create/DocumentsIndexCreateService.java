package ro.go.adrhc.persistence.lucene.index.create;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.core.write.DocumentsIndexWriter;
import ro.go.adrhc.persistence.lucene.index.core.write.DocumentsIndexWriterTemplate;
import ro.go.adrhc.persistence.lucene.index.core.write.FSIndexWriterHolder;
import ro.go.adrhc.util.collection.StreamCounter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class DocumentsIndexCreateService implements IndexCreateService<Document> {
	private final DocumentsIndexWriterTemplate indexWriterTemplate;
	private final Path indexPath;

	/**
	 * constructor parameters union
	 */
	public static DocumentsIndexCreateService create(FSIndexWriterHolder fsWriterHolder) {
		return new DocumentsIndexCreateService(
				new DocumentsIndexWriterTemplate(fsWriterHolder.getIndexWriter()),
				fsWriterHolder.getIndexPath());
	}

	public void createOrReplace(Stream<Document> documents) throws IOException {
		indexWriterTemplate.useWriter(writer -> doCreateOrReplace(documents, writer));
	}

	protected void doCreateOrReplace(Stream<Document> documents, DocumentsIndexWriter writer) throws IOException {
		log.debug("\nremoving all documents from {} ...", indexPath);
		writer.deleteAll();
		addDocuments(documents, writer);
	}

	protected void addDocuments(Stream<Document> documents, DocumentsIndexWriter writer) throws IOException {
		StreamCounter sc = new StreamCounter();
		log.debug("\nadding documents ...");
		writer.addDocuments(sc.countedStream(documents));
		log.debug("\nadded {} documents", sc.getCount());
	}
}
