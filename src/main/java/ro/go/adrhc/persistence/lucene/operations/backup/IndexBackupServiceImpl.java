package ro.go.adrhc.persistence.lucene.operations.backup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;

@RequiredArgsConstructor
@Slf4j
public class IndexBackupServiceImpl implements IndexBackupService {
	private final IndexWriter indexWriter;

	public void backup(Path indexBackupPath) throws IOException {
		Directory sourceDir = indexWriter.getDirectory();
		try (FSDirectory fsDirectory = FSDirectory.open(indexBackupPath)) {
			for (String fileName : sourceDir.listAll()) {
				fsDirectory.copyFrom(sourceDir, fileName, fileName, null);
			}
		}
	}
}
