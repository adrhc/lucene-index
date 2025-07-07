package ro.go.adrhc.persistence.lucene.operations.backup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexCommit;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.SnapshotDeletionPolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;

import java.io.IOException;
import java.nio.file.Path;

@RequiredArgsConstructor
@Slf4j
public class IndexBackupServiceImpl implements IndexBackupService {
	private final IndexWriter indexWriter;

	public void backup(Path indexBackupPath) throws IOException {
		SnapshotDeletionPolicy sdp = (SnapshotDeletionPolicy)
			indexWriter.getConfig().getIndexDeletionPolicy();
		IndexCommit snapshot = sdp.snapshot(); // 1️⃣ pin current commit
		try (FSDirectory target = FSDirectory.open(indexBackupPath);
		     Directory source = snapshot.getDirectory()) {
			// 2️⃣ copy exactly the files in the snapshot
			for (String file : snapshot.getFileNames()) {
				target.copyFrom(source, file, file, IOContext.DEFAULT);
			}
		} finally {
			sdp.release(snapshot); // 3️⃣ allow cleanup
		}
	}
}
