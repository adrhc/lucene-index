package ro.go.adrhc.persistence.lucene.operations.params;

import lombok.AllArgsConstructor;
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
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderParamsImpl;
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

import static ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParamsImpl.allHits;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@Slf4j
public class IndexServicesParamsFactoryImpl<T> implements IndexServicesParamsFactory<T> {
	private final Class<T> type;
	private final LuceneFieldSpec<T> idField;
	private final Collection<? extends LuceneFieldSpec<T>> typedFields;
	private final Analyzer analyzer;
	private final IndexReaderPool indexReaderPool;
	private final IndexWriter indexWriter;
	private final SearchResultFilter<T> searchResultFilter;
	private final Path indexPath;
	private final int searchHits;
	private boolean closed;

	@Override
	public IndexSearchServiceParams<T> indexSearchServiceParams() {
		return new IndexSearchServiceParamsImpl<>(type,
			idField, indexReaderPool, searchResultFilter, searchHits);
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
	public TypedIndexReaderParams<T> oneHitIndexReaderParams() {
		return new TypedIndexReaderParamsImpl<>(type, indexReaderPool);
	}

	@Override
	public DocIndexReaderParams indexCountServiceParams() {
		return new DocIndexReaderParamsImpl(indexReaderPool);
	}

	@Override
	public boolean isReadOnly() {
		return indexWriter == null;
	}

	@Override
	public HitsLimitedIndexReaderParams<T> allHitsTypedIndexReaderParams() {
		return allHits(type(), idField(), indexReaderPool);
	}

	@Override
	public void close() throws IOException {
		if (closed) {
			log.info("\nIndex already closed: {}", indexPath);
			return;
		}
		log.info("\nclosing {} ...", indexPath);
		indexReaderPool.close();
		log.info("\nIndexReaderPool closed!");
		if (isReadOnly()) {
			log.info("\nWon't close IndexWriter because the index was opened in read-only mode!");
		} else {
			log.info("\nclosing IndexWriter ...");
			indexWriter.close();
			log.info("\nIndexWriter closed!");
		}
		closed = true;
	}
}
