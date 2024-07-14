package ro.go.adrhc.persistence.lucene.operations.params;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.read.OneHitIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexUpsertParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexWriterParams;
import ro.go.adrhc.persistence.lucene.operations.restore.IndexShallowUpdateServiceParams;
import ro.go.adrhc.persistence.lucene.operations.retrieve.IndexRetrieveServiceParams;
import ro.go.adrhc.persistence.lucene.operations.search.IndexSearchServiceParams;

import java.io.Closeable;
import java.nio.file.Path;

public interface IndexServicesParamsFactory<T> extends Closeable {
	LuceneFieldSpec<T> getIdField();

	Analyzer getAnalyzer();

	IndexWriter getIndexWriter();

	IndexReaderPool getIndexReaderPool();

	Path getIndexPath();

	boolean isReadOnly();

	IndexSearchServiceParams<T> indexSearchServiceParams();

	IndexRetrieveServiceParams<T> typedRetrieveServiceParams();

	IndexShallowUpdateServiceParams<T> typedShallowUpdateServiceParams();

	TypedIndexWriterParams<T> typedIndexWriterParams();

	TypedIndexWriterParams<T> typedAddServiceParams();

	TypedIndexUpsertParams<T> typedIndexUpsertParams();

	TypedIndexRemoverParams typedIndexRemoverParams();

	OneHitIndexReaderParams<T> oneHitIndexReaderParams();

	HitsLimitedIndexReaderParams<T> allHitsTypedIndexReaderParams();
}
