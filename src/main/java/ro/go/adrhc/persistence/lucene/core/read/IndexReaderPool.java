package ro.go.adrhc.persistence.lucene.core.read;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.AlreadyClosedException;

import java.io.Closeable;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class IndexReaderPool implements Closeable {
	private final IndexWriter indexWriter;
	private DirectoryReader directoryReader;

	public IndexReader get() throws IOException {
		getReader();
		directoryReader.incRef();
		return directoryReader;
	}

	protected void getReader() throws IOException {
		if (directoryReader == null) {
			directoryReader = DirectoryReader.open(indexWriter);
		} else {
			DirectoryReader previousIndexReader = directoryReader;
			if (previousIndexReader.isCurrent()) {
				return;
			}
			directoryReader = DirectoryReader.openIfChanged(directoryReader);
			closeIfUnused(previousIndexReader);
		}
	}

	public void returnReader(IndexReader indexReader) throws IOException {
		indexReader.decRef();
		if (directoryReader != indexReader) {
			closeIfUnused(indexReader);
		}
	}

	protected void closeIfUnused(IndexReader indexReader) {
		if (indexReader.getRefCount() == 1) {
			IOUtils.closeQuietly(indexReader);
		}
	}

	@Override
	public void close() throws IOException {
		assert directoryReader.getRefCount() == 1 : "directoryReader refCount should be 1!";
		try {
			directoryReader.close();
		} catch (AlreadyClosedException ac) {
			// ignoring
		}
	}
}
