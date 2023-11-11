package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.write.IndexWriterFactory;
import ro.go.adrhc.persistence.lucene.typedindex.domain.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedindex.search.result.filter.QuerySearchResultFilter;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;

@Getter
@RequiredArgsConstructor
public class TypedIndexContext<ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
		implements Closeable {
	public static final int NUM_HITS = 10;
	private final Class<T> type;
	private final Class<E> tFieldEnumClass;
	private final E idField;
	private final IndexWriter indexWriter;
	private final IndexReaderPool indexReaderPool;
	private final QuerySearchResultFilter<T> searchResultFilter;
	private final int numHits;
	private final Path indexPath;

	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
	TypedIndexContext<ID, T, E> create(Class<T> tClass, Class<E> tFieldEnumClass,
			Analyzer analyzer, QuerySearchResultFilter<T> searchResultFilter, Path indexPath) throws IOException {
		IndexWriter indexWriter = IndexWriterFactory.fsWriter(analyzer, indexPath);
		IndexReaderPool indexReaderPool = new IndexReaderPool(indexWriter);
		E idField = TypedField.getIdField(tFieldEnumClass);
		return new TypedIndexContext<>(tClass, tFieldEnumClass, idField,
				indexWriter, indexReaderPool, searchResultFilter, NUM_HITS, indexPath);
	}

	public Analyzer getAnalyzer() {
		return indexWriter.getAnalyzer();
	}

	public void close() throws IOException {
		IOException exc = null;
		try {
			indexWriter.close();
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
