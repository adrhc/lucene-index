package ro.go.adrhc.persistence.lucene.operations.params;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexUpsertParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexWriterParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.shallowupdate.IndexShallowUpdateServiceParams;
import ro.go.adrhc.persistence.lucene.operations.retrieve.IndexRetrieveServiceParams;
import ro.go.adrhc.persistence.lucene.operations.search.IndexSearchServiceParams;

import java.io.Closeable;
import java.nio.file.Path;

public interface IndexServicesParamsFactory<T>
	extends TypedIndexWriterParams<T>, TypedIndexRemoverParams, TypedIndexUpsertParams<T>,
	IndexShallowUpdateServiceParams<T>, Closeable {
	LuceneFieldSpec<T> idField();

	Analyzer analyzer();

	IndexWriter indexWriter();

	Path indexPath();

	boolean isReadOnly();

	IndexSearchServiceParams<T> indexSearchServiceParams();

	IndexRetrieveServiceParams<T> typedRetrieveServiceParams();

	TypedIndexReaderParams<T> oneHitIndexReaderParams();

	DocIndexReaderParams docIndexReaderParams();
}
