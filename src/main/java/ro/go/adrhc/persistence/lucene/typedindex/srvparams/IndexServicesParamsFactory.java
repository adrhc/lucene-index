package ro.go.adrhc.persistence.lucene.typedindex.srvparams;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.typedcore.read.OneHitIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexUpsertParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexWriterParams;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexShallowUpdateServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.IndexRetrieveServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.search.IndexSearchServiceParams;

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

	TypedIndexReaderParams<T> allHitsTypedIndexReaderParams();
}
