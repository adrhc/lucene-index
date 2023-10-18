package ro.go.adrhc.persistence.lucene;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.tokenizer.LuceneTokenizer;
import ro.go.adrhc.persistence.lucene.write.DocumentIndexWriter;
import ro.go.adrhc.persistence.lucene.write.DocumentIndexWriterTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

import static ro.go.adrhc.util.ConversionUtils.convertAll;

@RequiredArgsConstructor
@Slf4j
public class IndexAdmin<T> {
	private final Path indexPath;
	private final SneakyFunction<T, Optional<Document>, IOException> toDocumentConverter;
	private final DocumentIndexWriterTemplate indexWriterTemplate;

	public static <T> IndexAdmin<T> create(Path indexPath, LuceneTokenizer luceneTokenizer,
			SneakyFunction<T, Optional<Document>, IOException> toDocumentConverter) {
		return new IndexAdmin<>(indexPath, toDocumentConverter, DocumentIndexWriterTemplate.ramWriterTemplate(luceneTokenizer.analyzer()));
	}

	public void createOrReplaceIndex(Collection<T> items) throws IOException {
		doCreateOrReplaceIndex(convertAll(toDocumentConverter, items));
	}

	protected void doCreateOrReplaceIndex(Collection<Document> documents) throws IOException {
		log.debug("\nremoving {} index (if exists) ...", indexPath);
		FileUtils.deleteDirectory(indexPath.toFile());
		log.debug("\nindexing in memory ...");
		indexWriterTemplate.useWriter(writer -> doCreateOrReplaceIndex(documents, writer));
	}

	protected void doCreateOrReplaceIndex(
			Collection<Document> documents, DocumentIndexWriter writer) throws IOException {
		log.debug("\nwriting {} documents to the index ...", documents.size());
		writer.addDocuments(documents);
		writer.flush();
		log.debug("\nsaving the index directory to {} ...", indexPath);
		writer.copyTo(indexPath);
	}
}
