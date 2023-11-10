package ro.go.adrhc.persistence.lucene.index.core.write;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.index.core.write.IndexWriterFactory.fsWriter;

@Getter
@RequiredArgsConstructor
public class FSIndexWriterHolder implements Closeable {
	private final Analyzer analyzer;
	private final IndexWriter indexWriter;
	private final Path indexPath;

	public static FSIndexWriterHolder create(Analyzer analyzer, Path indexPath) throws IOException {
		return new FSIndexWriterHolder(analyzer, fsWriter(analyzer, indexPath), indexPath);
	}

	@Override
	public void close() throws IOException {
		indexWriter.close();
	}
}
