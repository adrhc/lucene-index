package ro.go.adrhc.persistence.lucene.index;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.core.write.DocumentIndexWriterTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

@Slf4j
@Getter
public class FSLuceneIndex extends LuceneIndex {
	private final Path indexPath;

	public FSLuceneIndex(String idFieldName,
			DocumentIndexWriterTemplate indexWriterTemplate, Path indexPath) {
		super(idFieldName, indexWriterTemplate);
		this.indexPath = indexPath;
	}

	public void createOrReplace(Collection<Document> documents) throws IOException {
		log.debug("\nremoving {} index (if exists) ...", indexPath);
		FileUtils.deleteDirectory(indexPath.toFile());
		log.debug("\nindexing ...");
		addDocuments(documents);
	}
}
