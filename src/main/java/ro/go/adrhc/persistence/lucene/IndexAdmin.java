package ro.go.adrhc.persistence.lucene;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
public class IndexAdmin<T> {
	private final IndexUpdater<T> indexUpdater;
	private final Path indexPath;

	public void createOrReplaceIndex(Collection<T> items) throws IOException {
		log.debug("\nremoving {} index (if exists) ...", indexPath);
		FileUtils.deleteDirectory(indexPath.toFile());
		log.debug("\nindexing ...");
		indexUpdater.addItems(items);
	}
}
