package ro.go.adrhc.persistence.lucene.typedindex.servicesfactory;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.typedcore.read.OneHitIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexUpsertParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexWriterParams;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedShallowUpdateServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedRetrieveServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.search.IndexSearchServiceParams;

import java.io.Closeable;
import java.nio.file.Path;

public interface TypedIndexParams<T> extends Closeable {
	LuceneFieldSpec<T> getIdField();

	Analyzer getAnalyzer();

	IndexWriter getIndexWriter();

	IndexReaderPool getIndexReaderPool();

	Path getIndexPath();

	boolean isReadOnly();

	IndexSearchServiceParams<T> toTypedIndexSearchServiceParams();

	TypedRetrieveServiceParams<T> toTypedRetrieveServiceParams();

	TypedShallowUpdateServiceParams<T> toTypedShallowUpdateServiceParams();

	TypedIndexWriterParams<T> toTypedResetServiceParams();

	TypedIndexWriterParams<T> toTypedAddServiceParams();

	TypedIndexUpsertParams<T> toTypedIndexUpsertParams();

	TypedIndexRemoverParams toTypedRemoveServiceParams();

	OneHitIndexReaderParams<T> toOneHitIndexReaderTemplate();

	TypedIndexReaderParams<T> toAllHitsTypedIndexReaderParams();
}
