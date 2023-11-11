package ro.go.adrhc.persistence.lucene.core.write;

import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

@RequiredArgsConstructor
public class DocsIndexWriterTemplate {
	private final IndexWriter indexWriter;

	public <E extends Exception> void useWriter(
			SneakyConsumer<DocsIndexWriter, E> indexWriterConsumer)
			throws IOException, E {
		try (DocsIndexWriter indexWriter = new DocsIndexWriter(this.indexWriter)) {
			indexWriterConsumer.accept(indexWriter);
		}
	}
}
