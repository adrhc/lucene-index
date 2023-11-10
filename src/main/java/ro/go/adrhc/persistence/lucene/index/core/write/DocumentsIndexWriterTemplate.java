package ro.go.adrhc.persistence.lucene.index.core.write;

import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

@RequiredArgsConstructor
public class DocumentsIndexWriterTemplate {
	private final IndexWriter indexWriter;

	public <E extends Exception> void useWriter(
			SneakyConsumer<DocumentsIndexWriter, E> indexWriterConsumer)
			throws IOException, E {
		try (DocumentsIndexWriter indexWriter = new DocumentsIndexWriter(this.indexWriter)) {
			indexWriterConsumer.accept(indexWriter);
		}
	}
}
