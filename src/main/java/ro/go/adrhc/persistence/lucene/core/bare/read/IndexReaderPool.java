package ro.go.adrhc.persistence.lucene.core.bare.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakySupplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

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
			indexPath().ifPresentOrElse(
				p -> log.info("\nIndexReaderPool ({}) was never used!", p),
				() -> log.info("\nIndexReaderPool was never used!")
			);
		} else {
			safelyCloseIfRefIs1OrDecRefIfMore(directoryReader);
			indexPath().ifPresentOrElse(
				p -> log.info("\nIndexReader ({}) ref count: {}", p, directoryReader.getRefCount()),
				() -> log.info("\nIndexReader ref count: {}", directoryReader.getRefCount())
			);
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

	private void safelyCloseIfRefIs1OrDecRefIfMore(@NonNull IndexReader indexReader) {
		if (indexReader.getRefCount() == 1) {
			IOUtils.closeQuietly(indexReader);
		} else if (indexReader.getRefCount() > 1) {
			safelyDecRef(indexReader);
		}
	}

	private void safelyDecRef(@NonNull IndexReader indexReader) {
		try {
			indexReader.decRef();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			indexPath().ifPresent(p -> log.info("\nIndexReader ({}) decRef failed!", p));
		}
	}

	private Optional<Path> indexPath() {
		if (directoryReader.directory() instanceof FSDirectory dir) {
			return Optional.ofNullable(dir.getDirectory());
		} else {
			return Optional.empty();
		}
	}
}
