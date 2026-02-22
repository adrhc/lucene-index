package ro.go.adrhc.persistence.lucene.core.bare.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakyBiConsumer;
import org.apache.lucene.index.IndexWriter;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.core.bare.write.IndexWriterFactory;

import java.io.IOException;
import java.nio.file.Path;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractFSDocIndexTest {
	@TempDir
	protected static Path tmpDir;

	<E extends Exception> void useIndex(
		SneakyBiConsumer<IndexReaderPool, IndexWriter, E> testFn) throws IOException, E {
		try (IndexWriter writer = IndexWriterFactory.fsWriter(tmpDir)) {
			writer.commit(); // creates the index
			try (IndexReaderPool pool = IndexReaderPoolFactory.of(writer)) {
				testFn.accept(pool, writer);
			}
		}
	}
}
