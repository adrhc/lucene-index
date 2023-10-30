package ro.go.adrhc.persistence.lucene.index.create;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.core.write.DocumentIndexWriterTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import static ro.go.adrhc.persistence.lucene.index.core.write.DocumentIndexWriterTemplate.fsWriterTemplate;

@RequiredArgsConstructor
@Slf4j
public class DocumentsIndexCreateService implements IndexCreateService<Document> {
	private final DocumentIndexWriterTemplate indexWriterTemplate;
	private final Path indexPath;

	/**
	 * constructor parameters union
	 */
	public static DocumentsIndexCreateService create(Analyzer analyzer, Path indexPath) {
		return new DocumentsIndexCreateService(fsWriterTemplate(analyzer, indexPath), indexPath);
	}

	public void createOrReplace(Stream<Document> documents) throws IOException {
		log.debug("\nremoving {} index (if exists) ...", indexPath);
		FileUtils.deleteDirectory(indexPath.toFile());
		log.debug("\nindexing ...");
		indexWriterTemplate.useWriter(writer -> writer.addDocuments(documents));
	}
}
