package ro.go.adrhc.persistence.lucene.core.bare.write;

import lombok.experimental.UtilityClass;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.FSDirectory;
import ro.go.adrhc.persistence.lucene.core.bare.analysis.AnalyzerFactory;

import java.io.IOException;
import java.nio.file.Path;

@UtilityClass
public class IndexWriterFactory {
	public static IndexWriter fsWriter(Path indexPath) throws IOException {
		Analyzer analyzer = AnalyzerFactory.defaultAnalyzer()
			.orElseThrow(() -> new IOException("Failed to create the default analyzer!"));
		return fsWriter(analyzer, indexPath);
	}

	public static IndexWriter fsWriter(Analyzer analyzer, Path indexPath) throws IOException {
		IndexWriterConfig config = createOrAppendConfig(analyzer);
		return new IndexWriter(FSDirectory.open(indexPath), config);
	}

	public static IndexWriter ramWriter(Analyzer analyzer) throws IOException {
		IndexWriterConfig config = createOrAppendConfig(analyzer);
		return new IndexWriter(new ByteBuffersDirectory(), config);
	}

	private static IndexWriterConfig createOrAppendConfig(Analyzer analyzer) {
		return new IndexWriterConfig(analyzer)
			.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND)
			.setIndexDeletionPolicy(createIndexDeletionPolicy());
	}

	private static IndexDeletionPolicy createIndexDeletionPolicy() {
		IndexDeletionPolicy keepLast = new KeepOnlyLastCommitDeletionPolicy();
		return new SnapshotDeletionPolicy(keepLast);
	}
}
