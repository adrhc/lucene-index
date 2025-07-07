package ro.go.adrhc.persistence.lucene.operations.backup;

import java.io.IOException;
import java.nio.file.Path;

@FunctionalInterface
public interface IndexBackupService {
	void backup(Path indexBackupPath) throws IOException;
}
