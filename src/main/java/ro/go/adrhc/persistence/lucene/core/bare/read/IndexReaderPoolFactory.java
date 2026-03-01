package ro.go.adrhc.persistence.lucene.core.bare.read;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import ro.go.adrhc.persistence.lucene.core.bare.write.IndexWriterFactory;

import java.io.IOException;
import java.nio.file.Path;

@UtilityClass
@Slf4j
public class IndexReaderPoolFactory {
	public static IndexReaderPool of(IndexWriter writer) {
		return new IndexReaderPool(() -> DirectoryReader.open(writer));
	}

	public static IndexReaderPool of(Path indexPath) {
		return new IndexReaderPool(() -> {
			createIfMissing(indexPath);
			FSDirectory directory = FSDirectory.open(indexPath);
			if (DirectoryReader.indexExists(directory)) {
				return DirectoryReader.open(directory);
			} else {
				log.warn("\n{} is an empty index!", indexPath);
				return null;
			}
		});
	}

	private static void createIfMissing(Path indexPath) throws IOException {
		try (FSDirectory directory = FSDirectory.open(indexPath)) {
			if (!DirectoryReader.indexExists(directory)) {
				try (var indWriter = IndexWriterFactory.fsWriter(directory)) {
					log.warn("\n{} is missing, creating it!", indexPath);
				}
			}
		}
	}
}
