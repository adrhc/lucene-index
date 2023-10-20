package ro.go.adrhc.persistence.lucene;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.write.DocumentIndexWriterTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

import static ro.go.adrhc.persistence.lucene.write.DocumentIndexWriterTemplate.fsWriterTemplate;

@Slf4j
@Getter
public class FSTypedIndex<T> extends TypedIndex<T> {
	private final Path indexPath;

	public FSTypedIndex(String idFieldName,
			SneakyFunction<T, Optional<Document>, IOException> toDocumentConverter,
			DocumentIndexWriterTemplate indexWriterTemplate, Path indexPath) {
		super(idFieldName, toDocumentConverter, indexWriterTemplate);
		this.indexPath = indexPath;
	}

	public static <T> FSTypedIndex<T> createFSIndex(Enum<?> idField, Analyzer analyzer,
			SneakyFunction<T, Optional<Document>, IOException> toDocumentConverter, Path indexPath) {
		return new FSTypedIndex<>(idField.name(), toDocumentConverter,
				fsWriterTemplate(analyzer, indexPath), indexPath);
	}

	public void createOrReplace(Collection<T> items) throws IOException {
		log.debug("\nremoving {} index (if exists) ...", indexPath);
		FileUtils.deleteDirectory(indexPath.toFile());
		log.debug("\nindexing ...");
		addItems(items);
	}
}
