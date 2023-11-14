package ro.go.adrhc.persistence.lucene.core.write;

import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

@RequiredArgsConstructor
public class DocsIndexWriterTemplate {
	private final DocsIndexWriter indexWriter;

	public static DocsIndexWriterTemplate create(IndexWriter indexWriter) {
		return new DocsIndexWriterTemplate(new DocsIndexWriter(indexWriter));
	}

	public <E extends Exception> void useWriter(
			SneakyConsumer<DocsIndexWriter, E> indexWriterConsumer)
			throws IOException, E {
		try (DocsIndexWriter indexWriter = this.indexWriter) {
			indexWriterConsumer.accept(indexWriter);
		}
	}
}
