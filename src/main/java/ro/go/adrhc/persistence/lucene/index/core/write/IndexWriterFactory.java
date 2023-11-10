package ro.go.adrhc.persistence.lucene.index.core.write;

import lombok.experimental.UtilityClass;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.index.core.write.IndexWriterConfigFactories.createOrAppend;

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
}
