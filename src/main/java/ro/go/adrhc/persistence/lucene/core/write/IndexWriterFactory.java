package ro.go.adrhc.persistence.lucene.core.write;

import lombok.experimental.UtilityClass;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;

@UtilityClass
public class IndexWriterFactory {
	public static IndexWriter fsWriter(Analyzer analyzer, Path indexPath) throws IOException {
		IndexWriterConfig config = createOrAppend(analyzer);
		return new IndexWriter(FSDirectory.open(indexPath), config);
	}

	public static IndexWriter ramWriter(Analyzer analyzer) throws IOException {
		IndexWriterConfig config = createOrAppend(analyzer);
		return new IndexWriter(new ByteBuffersDirectory(), config);
	}

	private static IndexWriterConfig createOrAppend(Analyzer analyzer) {
		return new IndexWriterConfig(analyzer)
				.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
	}
}
