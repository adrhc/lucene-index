package ro.go.adrhc.persistence.lucene.index.core.write;

import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;
import com.rainerhahnekamp.sneakythrow.functional.SneakySupplier;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;

import java.io.IOException;
import java.nio.file.Path;

@RequiredArgsConstructor
public class DocumentsIndexWriterTemplate {
	private final SneakySupplier<DocumentsIndexWriter, ? extends IOException> writerSupplier;

	public static DocumentsIndexWriterTemplate fsWriterTemplate(Analyzer analyzer, Path indexPath) {
		return new DocumentsIndexWriterTemplate(() -> DocumentsIndexWriter.fsWriter(analyzer, indexPath));
	}

	public static DocumentsIndexWriterTemplate ramWriterTemplate(Analyzer analyzer) {
		return new DocumentsIndexWriterTemplate(() -> DocumentsIndexWriter.ramWriter(analyzer));
	}

	public <E extends Exception> void useWriter(
			SneakyConsumer<DocumentsIndexWriter, E> indexWriterConsumer)
			throws IOException, E {
		try (DocumentsIndexWriter indexWriter = writerSupplier.get()) {
			indexWriterConsumer.accept(indexWriter);
		}
	}
}
