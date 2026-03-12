package ro.go.adrhc.persistence.lucene.operation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexReaderParamsImpl;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.field.RawFieldValueSerdes;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderParamsImpl;
import ro.go.adrhc.persistence.lucene.service.search.IndexSearchServiceParams;
import ro.go.adrhc.persistence.lucene.service.search.IndexSearchServiceParamsImpl;
import ro.go.adrhc.persistence.lucene.service.search.SearchResultFilter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@Slf4j
public class IndexOperationsParamsImpl<T> implements IndexOperationsParams<T> {
	private final Class<T> type;
	private final LuceneFieldSpec<T> idField;
	private final Collection<? extends LuceneFieldSpec<T>> typedFields;
	private final Analyzer analyzer;
	private final IndexReaderPool indexReaderPool;
	private final IndexWriter indexWriter;
	private final RawFieldValueSerdes<T> rawFieldValueSerdes;
	private final SearchResultFilter<T> searchResultFilter;
	private final Path indexPath;
	private boolean closed;

	@Override
	public IndexSearchServiceParams<T> indexSearchServiceParams() {
		return new IndexSearchServiceParamsImpl<>(type, idField,
			indexReaderPool, rawFieldValueSerdes, searchResultFilter);
	}

	@Override
	public TypedIndexReaderParams<T> typedIndexReaderParams() {
		return new TypedIndexReaderParamsImpl<>(idField, indexReaderPool, rawFieldValueSerdes);
	}

	@Override
	public DocIndexReaderParams docIndexReaderParams() {
		return new DocIndexReaderParamsImpl(indexReaderPool);
	}

	@Override
	public boolean isReadOnly() {
		return indexWriter == null;
	}

	@Override
	public void close() throws IOException {
		if (closed) {
			log.info("\nIndex already closed: {}", indexPathLabel());
			return;
		}
		log.info("\nclosing {} ...", indexPathLabel());
		indexReaderPool.close();
		log.info("\nIndexReaderPool closed!");
		if (isReadOnly()) {
			log.info("\nWon't close IndexWriter because the index was opened in read-only mode!");
		} else {
			log.info("\nclosing {} writer ...", indexPathLabel());
			indexWriter.close();
			log.info("\n{} writer closed!", indexPathLabel());
		}
		closed = true;
	}

	private String indexPathLabel() {
		return indexPath == null ? "RAM index" : indexPath.toString();
	}
}
