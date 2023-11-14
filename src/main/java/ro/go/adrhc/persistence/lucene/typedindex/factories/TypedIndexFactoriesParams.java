package ro.go.adrhc.persistence.lucene.typedindex.factories;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreAndTyped;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexUpdaterParams;
import ro.go.adrhc.persistence.lucene.typedindex.create.TypedIndexInitServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedIndexRetrieveServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.search.SearchResultFilter;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchServiceParams;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class TypedIndexFactoriesParams<ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
		implements Closeable, TypedIndexSearchServiceParams<T>, TypedIndexRestoreServiceParams<T>,
		TypedIndexInitServiceParams<T>, TypedIndexRetrieveServiceParams<T>, TypedIndexUpdaterParams<T> {
	private final Class<T> type;
	private final Class<E> tFieldEnumClass;
	private final Collection<? extends TypedField<T>> typedFields;
	private final E idField;
	private final Analyzer analyzer;
	private final IndexWriter indexWriter;
	private final IndexReaderPool indexReaderPool;
	private final int numHits;
	private final SearchResultFilter<ScoreAndTyped<T>> searchResultFilter;
	private final Path indexPath;

	@Override
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
