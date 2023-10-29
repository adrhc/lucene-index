package ro.go.adrhc.persistence.lucene.fsindex;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.core.docds.datasource.DocumentsDataSource;
import ro.go.adrhc.persistence.lucene.index.core.write.DocumentIndexWriterTemplate;

import java.io.IOException;
import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.index.core.write.DocumentIndexWriterTemplate.fsWriterTemplate;

@RequiredArgsConstructor
@Slf4j
public class FSIndexCreateService {
	private final DocumentsDataSource documentsDatasource;
	private final DocumentIndexWriterTemplate indexWriterTemplate;
	private final Path indexPath;

	public static FSIndexCreateService create(
			DocumentsDataSource documentsDatasource, Analyzer analyzer, Path indexPath) {
		return new FSIndexCreateService(documentsDatasource,
				fsWriterTemplate(analyzer, indexPath), indexPath);
	}

	public void createOrReplace() throws IOException {
		Iterable<Document> documents = documentsDatasource.loadAll();
		log.debug("\nremoving {} index (if exists) ...", indexPath);
		FileUtils.deleteDirectory(indexPath.toFile());
		log.debug("\nindexing ...");
		indexWriterTemplate.useWriter(writer -> writer.addDocuments(documents));
	}
}
