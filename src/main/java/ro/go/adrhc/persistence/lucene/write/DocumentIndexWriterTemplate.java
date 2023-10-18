package ro.go.adrhc.persistence.lucene.write;

import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;
import com.rainerhahnekamp.sneakythrow.functional.SneakySupplier;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;

import java.io.IOException;
import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.write.DocumentIndexWriter.fsWriter;
import static ro.go.adrhc.persistence.lucene.write.DocumentIndexWriter.ramWriter;

@RequiredArgsConstructor
public class DocumentIndexWriterTemplate {
	private final SneakySupplier<DocumentIndexWriter, ? extends IOException> writerSupplier;

	public static DocumentIndexWriterTemplate fsWriterTemplate(Analyzer analyzer, Path indexPath) {
		return new DocumentIndexWriterTemplate(() -> fsWriter(analyzer, indexPath));
	}

	public static DocumentIndexWriterTemplate ramWriterTemplate(Analyzer analyzer) {
		return new DocumentIndexWriterTemplate(() -> ramWriter(analyzer));
	}

	public <E extends Exception> void useWriter(
			SneakyConsumer<DocumentIndexWriter, E> indexWriterConsumer)
			throws IOException, E {
		try (DocumentIndexWriter indexWriter = writerSupplier.get()) {
			indexWriterConsumer.accept(indexWriter);
		}
	}
}
