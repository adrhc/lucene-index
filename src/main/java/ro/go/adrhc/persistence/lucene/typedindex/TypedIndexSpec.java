package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.index.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.index.core.write.FSIndexWriterHolder;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;

@Getter
@RequiredArgsConstructor
public class TypedIndexSpec<ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
		implements Closeable {
	public static final int NUM_HITS = 10;
	private final int numHits;
	private final Class<T> type;
	private final Class<E> tFieldEnumClass;
	private final E idField;
	private final FSIndexWriterHolder fsWriterHolder;
	private final IndexReaderPool indexReaderPool;

	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
	TypedIndexSpec<ID, T, E> create(Class<T> tClass, Class<E> tFieldEnumClass,
			FSIndexWriterHolder fsWriterHolder) throws IOException {
		IndexReaderPool indexReaderPool = IndexReaderPool.create(fsWriterHolder);
		E idField = TypedField.getIdField(tFieldEnumClass);
		return new TypedIndexSpec<>(NUM_HITS, tClass,
				tFieldEnumClass, idField, fsWriterHolder, indexReaderPool);
	}

	public Analyzer getAnalyzer() {
		return getFsWriterHolder().getAnalyzer();
	}

	public Path getIndexPath() {
		return getFsWriterHolder().getIndexPath();
	}

	public IndexWriter getIndexWriter() {
		return getFsWriterHolder().getIndexWriter();
	}

	public void close() throws IOException {
		IOException exc = null;
		try {
			fsWriterHolder.close();
		} catch (IOException e) {
			exc = e;
		}
		try {
			indexReaderPool.close();
		} catch (IOException e) {
			exc = exc == null ? e : exc;
		}
		if (exc != null) {
			throw exc;
		}
	}
}
