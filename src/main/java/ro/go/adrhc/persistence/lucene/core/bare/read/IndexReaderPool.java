package ro.go.adrhc.persistence.lucene.core.bare.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakySupplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.springframework.lang.NonNull;
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
		openIfChanged();
		if (directoryReader != null) {
			directoryReader.incRef();
		}
		return directoryReader;
	}

	public synchronized void dismissReader(IndexReader indexReader) throws IOException {
		indexReader.decRef();
		if (directoryReader != indexReader) {
			safelyCloseIfRefIs1OrDecRefIfMore(indexReader);
		}
	}

	@Override
	public synchronized void close() {
		if (directoryReader == null) {
			log.warn("\nIndexReaderPool was never used!");
		} else {
			safelyCloseIfRefIs1OrDecRefIfMore(directoryReader);
			log.info("IndexReader ref count: {}", directoryReader.getRefCount());
		}
	}

	protected void openIfChanged() throws IOException {
		if (directoryReader == null) {
			directoryReader = dirReaderSupplier.get();
		} else {
			DirectoryReader previousIndexReader = directoryReader;
			directoryReader = DirectoryReader.openIfChanged(directoryReader);
			if (directoryReader != null) {
				safelyCloseIfRefIs1OrDecRefIfMore(previousIndexReader);
			} else {
				directoryReader = previousIndexReader;
			}
		}
	}

	private static void safelyCloseIfRefIs1OrDecRefIfMore(@NonNull IndexReader indexReader) {
		if (indexReader.getRefCount() == 1) {
			IOUtils.closeQuietly(indexReader);
		} else if (indexReader.getRefCount() > 1) {
			safelyDecRef(indexReader);
		}
	}

	private static void safelyDecRef(@NonNull IndexReader indexReader) {
		try {
			indexReader.decRef();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
