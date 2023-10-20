package ro.go.adrhc.persistence.lucene;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.tokenizer.LuceneTokenizer;
import ro.go.adrhc.persistence.lucene.write.DocumentIndexWriterTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

import static ro.go.adrhc.persistence.lucene.write.DocumentIndexWriterTemplate.fsWriterTemplate;

@Slf4j
public class FSLuceneIndex<T> extends LuceneIndex<T> {
	private final Path indexPath;

	public FSLuceneIndex(String idFieldName,
			SneakyFunction<T, Optional<Document>, IOException> toDocumentConverter,
			DocumentIndexWriterTemplate indexWriterTemplate, Path indexPath) {
		super(idFieldName, toDocumentConverter, indexWriterTemplate);
		this.indexPath = indexPath;
	}

	public static <T> FSLuceneIndex<T> createFSIndex(
			Enum<?> idField, LuceneTokenizer luceneTokenizer,
			SneakyFunction<T, Optional<Document>, IOException> toDocumentConverter, Path indexPath) {
		return new FSLuceneIndex<>(idField.name(), toDocumentConverter,
				fsWriterTemplate(luceneTokenizer.analyzer(), indexPath), indexPath);
	}

	public void createOrReplace(Collection<T> items) throws IOException {
		log.debug("\nremoving {} index (if exists) ...", indexPath);
		FileUtils.deleteDirectory(indexPath.toFile());
		log.debug("\nindexing ...");
		addItems(items);
	}
}
