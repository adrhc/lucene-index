package ro.go.adrhc.persistence.lucene.core.bare.write;

import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;
import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.core.bare.write.IndexWriterFactory.fsWriter;
import static ro.go.adrhc.persistence.lucene.core.bare.write.IndexWriterFactory.ramWriter;

@RequiredArgsConstructor
public class DocsIndexWriterTemplate {
	private final DocsIndexWriter indexWriter;

	public static DocsIndexWriterTemplate
	ofRamWriter(Analyzer analyzer) throws IOException {
		return DocsIndexWriterTemplate.of(ramWriter(analyzer));
	}

	public static DocsIndexWriterTemplate ofFsWriter(
		Analyzer analyzer, Path indexPath) throws IOException {
		return DocsIndexWriterTemplate.of(fsWriter(analyzer, indexPath));
	}

	public static DocsIndexWriterTemplate of(IndexWriter indexWriter) {
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
