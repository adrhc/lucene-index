package ro.go.adrhc.persistence.lucene.core.bare.read;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Path;

@UtilityClass
@Slf4j
public class IndexReaderPoolFactory {
	public static IndexReaderPool ofRAM() {
		return new IndexReaderPool(() -> DirectoryReader.open(new ByteBuffersDirectory()));
	}

	public static IndexReaderPool of(Path indexPath) {
		return new IndexReaderPool(() -> {
			FSDirectory directory = FSDirectory.open(indexPath);
			if (DirectoryReader.indexExists(directory)) {
				return DirectoryReader.open(directory);
			} else {
				log.warn("\n{} is an empty index!", indexPath);
				return null;
			}
		});
	}
}
