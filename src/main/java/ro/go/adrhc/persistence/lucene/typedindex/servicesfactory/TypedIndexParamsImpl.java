package ro.go.adrhc.persistence.lucene.typedindex.servicesfactory;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.typedcore.read.OneHitIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.read.OneHitIndexReaderParamsImpl;
import ro.go.adrhc.persistence.lucene.typedcore.write.*;
import ro.go.adrhc.persistence.lucene.typedindex.AllHitsTypedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedShallowUpdateServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedShallowUpdateServiceParamsImpl;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedRetrieveServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedRetrieveServiceParamsImpl;
import ro.go.adrhc.persistence.lucene.typedindex.search.IndexSearchServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.search.IndexSearchServiceParamsImpl;
import ro.go.adrhc.persistence.lucene.typedindex.search.SearchResultFilter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

@Getter
@Slf4j
public class TypedIndexParamsImpl<T> extends AllHitsTypedIndexReaderParams<T>
		implements TypedIndexParams<T> {
	private final Collection<? extends LuceneFieldSpec<T>> typedFields;
	private final Analyzer analyzer;
	private final IndexWriter indexWriter;
	private final int searchHits;
	private final SearchResultFilter<T> searchResultFilter;
	private final Path indexPath;
	private boolean closed;

	public TypedIndexParamsImpl(Class<T> type, LuceneFieldSpec<T> idField,
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
	public IndexSearchServiceParams<T> toTypedIndexSearchServiceParams() {
		return new IndexSearchServiceParamsImpl<>(type, idField,
				indexReaderPool, searchResultFilter, searchHits);
	}

	@Override
	public TypedRetrieveServiceParams<T> toTypedRetrieveServiceParams() {
		return new TypedRetrieveServiceParamsImpl<>(type, idField, indexReaderPool);
	}

	@Override
	public TypedShallowUpdateServiceParams<T> toTypedShallowUpdateServiceParams() {
		return new TypedShallowUpdateServiceParamsImpl<>(type, idField,
				indexReaderPool, typedFields, analyzer, indexWriter);
	}

	@Override
	public TypedIndexWriterParams<T> toTypedResetServiceParams() {
		return new TypedIndexWriterParamsImpl<>(indexWriter, analyzer, typedFields);
	}

	@Override
	public TypedIndexWriterParams<T> toTypedAddServiceParams() {
		return new TypedIndexWriterParamsImpl<>(indexWriter, analyzer, typedFields);
	}

	@Override
	public TypedIndexUpsertParams<T> toTypedIndexUpsertParams() {
		return new TypedIndexUpsertParamsImpl<>(idField, indexWriter, analyzer, typedFields);
	}

	@Override
	public TypedIndexRemoverParams toTypedRemoveServiceParams() {
		return new TypedIndexRemoverParamsImpl(idField, indexWriter);
	}

	@Override
	public OneHitIndexReaderParams<T> toOneHitIndexReaderTemplate() {
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
