package ro.go.adrhc.persistence.lucene.operations.params;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.read.AllHitsTypedIndexReaderParamsFactory;
import ro.go.adrhc.persistence.lucene.core.typed.read.OneHitIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.read.OneHitIndexReaderParamsImpl;
import ro.go.adrhc.persistence.lucene.core.typed.write.*;
import ro.go.adrhc.persistence.lucene.operations.restore.IndexShallowUpdateServiceParams;
import ro.go.adrhc.persistence.lucene.operations.restore.IndexShallowUpdateServiceParamsImpl;
import ro.go.adrhc.persistence.lucene.operations.retrieve.IndexRetrieveServiceParams;
import ro.go.adrhc.persistence.lucene.operations.retrieve.IndexRetrieveServiceParamsImpl;
import ro.go.adrhc.persistence.lucene.operations.search.IndexSearchServiceParams;
import ro.go.adrhc.persistence.lucene.operations.search.IndexSearchServiceParamsImpl;
import ro.go.adrhc.persistence.lucene.operations.search.SearchResultFilter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

@Getter
@Slf4j
public class IndexServicesParamsFactoryImpl<T> extends AllHitsTypedIndexReaderParamsFactory<T>
		implements IndexServicesParamsFactory<T> {
	private final Collection<? extends LuceneFieldSpec<T>> typedFields;
	private final Analyzer analyzer;
	private final IndexWriter indexWriter;
	private final int searchHits;
	private final SearchResultFilter<T> searchResultFilter;
	private final Path indexPath;
	private boolean closed;

	public IndexServicesParamsFactoryImpl(Class<T> type, LuceneFieldSpec<T> idField,
			IndexReaderPool indexReaderPool,
			Collection<? extends LuceneFieldSpec<T>> typedFields, Analyzer analyzer,
			IndexWriter indexWriter,
			int searchHits, SearchResultFilter<T> searchResultFilter, Path indexPath) {
		super(type, idField, indexReaderPool);
		this.typedFields = typedFields;
		this.analyzer = analyzer;
		this.indexWriter = indexWriter;
		this.searchHits = searchHits;
		this.searchResultFilter = searchResultFilter;
		this.indexPath = indexPath;
	}

	@Override
	public IndexSearchServiceParams<T> indexSearchServiceParams() {
		return new IndexSearchServiceParamsImpl<>(type, idField,
				indexReaderPool, searchResultFilter, searchHits);
	}

	@Override
	public IndexRetrieveServiceParams<T> typedRetrieveServiceParams() {
		return new IndexRetrieveServiceParamsImpl<>(type, idField, indexReaderPool);
	}

	@Override
	public IndexShallowUpdateServiceParams<T> typedShallowUpdateServiceParams() {
		return new IndexShallowUpdateServiceParamsImpl<>(type, idField,
				indexReaderPool, typedFields, analyzer, indexWriter);
	}

	@Override
	public TypedIndexWriterParams<T> typedIndexWriterParams() {
		return new TypedIndexWriterParamsImpl<>(indexWriter, analyzer, typedFields);
	}

	@Override
	public TypedIndexWriterParams<T> typedAddServiceParams() {
		return new TypedIndexWriterParamsImpl<>(indexWriter, analyzer, typedFields);
	}

	@Override
	public TypedIndexUpsertParams<T> typedIndexUpsertParams() {
		return new TypedIndexUpsertParamsImpl<>(idField, indexWriter, analyzer, typedFields);
	}

	@Override
	public TypedIndexRemoverParams typedIndexRemoverParams() {
		return new TypedIndexRemoverParamsImpl(idField, indexWriter);
	}

	@Override
	public OneHitIndexReaderParams<T> oneHitIndexReaderParams() {
		return new OneHitIndexReaderParamsImpl<>(indexReaderPool, type);
	}

	public boolean isReadOnly() {
		return indexWriter == null;
	}

	@Override
	public void close() throws IOException {
		if (closed) {
			log.info("\nindex already closed: {}", indexPath);
			return;
		}
		log.info("\nclosing {} ...", indexPath);
		IOException exc = null;
		try {
			log.info("\nclosing IndexReaderPool ...");
			indexReaderPool.close();
			log.info("\nIndexReaderPool closed");
		} catch (IOException e) {
			log.error("\nIndexReaderPool failed to close!");
			exc = e;
		}
		if (isReadOnly()) {
			log.info("\nWon't close IndexWriter because the index was opened in read-only mode!");
		} else {
			try {
				log.info("\nclosing IndexWriter ...");
				indexWriter.close();
				log.info("\nIndexWriter closed");
			} catch (IOException e) {
				log.error("\nIndexWriter failed to close!");
				exc = e;
			}
		}
		closed = true;
		if (exc != null) {
			throw exc;
		}
	}
}
