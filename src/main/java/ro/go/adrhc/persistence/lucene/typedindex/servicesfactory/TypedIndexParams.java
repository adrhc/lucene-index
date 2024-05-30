package ro.go.adrhc.persistence.lucene.typedindex.servicesfactory;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.read.OneHitIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexUpdaterParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexWriterParams;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedIndexRetrieveServiceParams;
import ro.go.adrhc.persistence.lucene.typedindex.search.IndexSearchServiceParams;

import java.io.Closeable;
import java.nio.file.Path;

public interface TypedIndexParams<T> extends Closeable {
	TypedField<T> getIdField();

	Analyzer getAnalyzer();

	IndexWriter getIndexWriter();

	IndexReaderPool getIndexReaderPool();

	Path getIndexPath();

	boolean isReadOnly();

	IndexSearchServiceParams<T> toTypedIndexSearchServiceParams();

	TypedIndexRetrieveServiceParams<T> toTypedIndexRetrieveServiceParams();

	TypedIndexRestoreServiceParams<T> toTypedIndexRestoreServiceParams();

	TypedIndexWriterParams<T> toTypedIndexResetServiceParams();

	TypedIndexWriterParams<T> toTypedIndexAdderServiceParams();

	TypedIndexUpdaterParams<T> toTypedIndexUpsertServiceParams();

	TypedIndexRemoverParams toTypedIndexRemoveServiceParams();

	OneHitIndexReaderParams<T> toOneHitIndexReaderTemplate();

	TypedIndexReaderParams<T> toAllHitsTypedIndexReaderParams();
}
