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
			safelyCloseIfRefIs1(indexReader);
		}
	}

	@Override
	public synchronized void close() throws IOException {
		if (directoryReader == null) {
			log.warn("\nIndexReaderPool was never used!");
			return;
		}
		warnIfUsedElsewhere();
		decRefTo1();
		safelyCloseIfRefIs1(directoryReader);
	}

	protected void openIfChanged() throws IOException {
		if (directoryReader == null) {
			directoryReader = dirReaderSupplier.get();
		} else {
			DirectoryReader previousIndexReader = directoryReader;
			directoryReader = DirectoryReader.openIfChanged(directoryReader);
			if (directoryReader != null) {
				safelyCloseIfRefIs1(previousIndexReader);
			} else {
				directoryReader = previousIndexReader;
			}
		}
	}

	private void warnIfUsedElsewhere() {
		if (directoryReader.getRefCount() > 1) {
			log.error("\ndirectoryReader refCount should be 1 but is {}!",
				directoryReader.getRefCount());
		}
	}

	private void decRefTo1() throws IOException {
		while (directoryReader.getRefCount() > 1) {
			directoryReader.decRef();
		}
	}

	private static void safelyCloseIfRefIs1(@NonNull IndexReader indexReader) {
		if (indexReader.getRefCount() == 1) {
			IOUtils.closeQuietly(indexReader);
		}
	}
}
