package ro.go.adrhc.persistence.lucene.core.write;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;

@RequiredArgsConstructor
public class FSDocIndexWriter {
	private final IndexWriter indexWriter;

	public void copyTo(Path destination) throws IOException {
		Directory sourceDir = indexWriter.getDirectory();
		try (FSDirectory fsDirectory = FSDirectory.open(destination)) {
			for (String fileName : sourceDir.listAll()) {
				fsDirectory.copyFrom(sourceDir, fileName, fileName, null);
			}
		}
	}
}
