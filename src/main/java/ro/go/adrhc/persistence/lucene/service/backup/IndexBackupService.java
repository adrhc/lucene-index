package ro.go.adrhc.persistence.lucene.service.backup;

import java.io.IOException;
import java.nio.file.Path;

@FunctionalInterface
public interface IndexBackupService {
	void backup(Path indexBackupPath) throws IOException;
}
