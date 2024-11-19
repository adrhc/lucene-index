package ro.go.adrhc.persistence.lucene.core.bare.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakySupplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.AlreadyClosedException;
import org.springframework.lang.Nullable;

import java.io.Closeable;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class IndexReaderPool implements Closeable {
	private final SneakySupplier<DirectoryReader, IOException> dirReaderSupplier;
	private DirectoryReader directoryReader;

	@Nullable
	public synchronized IndexReader getReader() throws IOException {
		updateReader();
		if (directoryReader != null) {
			directoryReader.incRef();
		}
		return directoryReader;
	}

	public synchronized void dismissReader(IndexReader indexReader) throws IOException {
		indexReader.decRef();
		if (directoryReader != indexReader) {
			closeIfUnused(indexReader);
		}
	}

	@Override
	public synchronized void close() throws IOException {
		if (directoryReader == null) {
			log.warn("\nIndexReaderPool was never used!");
			return;
		}
		if (directoryReader.getRefCount() != 1) {
			log.error("\ndirectoryReader refCount should be 1 but is {}!",
					directoryReader.getRefCount());
		}
		try {
			directoryReader.close();
		} catch (AlreadyClosedException ac) {
			log.error("\nTried to close an already closed index!");
		}
	}

	protected void updateReader() throws IOException {
		if (directoryReader == null) {
			directoryReader = dirReaderSupplier.get();
		} else {
			DirectoryReader previousIndexReader = directoryReader;
			directoryReader = DirectoryReader.openIfChanged(directoryReader);
			if (directoryReader != null) {
				closeIfUnused(previousIndexReader);
			} else {
				directoryReader = previousIndexReader;
			}
		}
	}

	protected void closeIfUnused(IndexReader indexReader) {
		if (indexReader.getRefCount() == 1) {
			IOUtils.closeQuietly(indexReader);
		}
	}
}
