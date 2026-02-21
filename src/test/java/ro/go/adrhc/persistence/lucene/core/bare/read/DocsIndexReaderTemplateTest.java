package ro.go.adrhc.persistence.lucene.core.bare.read;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.core.bare.write.IndexWriterFactory;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DocsIndexReaderTemplateTest {
	@TempDir
	protected static Path tmpDir;

	@Test
	void useRAMReader() throws IOException {
		try (var writer = IndexWriterFactory.ramWriter()) {
			writer.commit();
			IndexReaderPool pool = IndexReaderPoolFactory.of(writer);
			DocsIndexReaderTemplate tmpl = DocsIndexReaderTemplateFactory.of(pool);
			Integer count = tmpl.useReader(DocsIndexReader::count);
			assertEquals(0, count);
			pool.close();
		}
	}

	@Test
	void useReader() throws IOException {
		try (var writer = IndexWriterFactory.fsWriter(tmpDir)) {
			writer.commit();
			IndexReaderPool pool = IndexReaderPoolFactory.of(tmpDir);
			DocsIndexReaderTemplate tmpl = DocsIndexReaderTemplateFactory.of(pool);
			Integer count = tmpl.useReader(DocsIndexReader::count);
			assertEquals(0, count);
			pool.close();
		}
	}
}