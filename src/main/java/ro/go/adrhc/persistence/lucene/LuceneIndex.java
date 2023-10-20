package ro.go.adrhc.persistence.lucene;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.tokenizer.LuceneTokenizer;
import ro.go.adrhc.persistence.lucene.write.DocumentIndexWriterTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ro.go.adrhc.persistence.lucene.write.DocumentIndexWriterTemplate.ramWriterTemplate;
import static ro.go.adrhc.util.ConversionUtils.convertAll;

@RequiredArgsConstructor
@Slf4j
public class LuceneIndex<T> {
	private final String idFieldName;
	private final SneakyFunction<T, Optional<Document>, IOException> toDocumentConverter;
	private final DocumentIndexWriterTemplate indexWriterTemplate;

	public static <T> LuceneIndex<T> createRAMIndex(
			Enum<?> idField, Path indexPath, LuceneTokenizer luceneTokenizer,
			SneakyFunction<T, Optional<Document>, IOException> toDocumentConverter) {
		return new LuceneIndex<>(idField.name(), toDocumentConverter,
				ramWriterTemplate(luceneTokenizer.analyzer()));
	}

	public void addItems(Collection<T> items) throws IOException {
		Collection<Document> documents = convertAll(toDocumentConverter, items);
		indexWriterTemplate.useWriter(writer -> writer.addDocuments(documents));
	}

	public void removeByIds(List<String> ids) throws IOException {
		indexWriterTemplate.useWriter(writer -> writer.removeByFieldValues(idFieldName, ids));
	}
}
