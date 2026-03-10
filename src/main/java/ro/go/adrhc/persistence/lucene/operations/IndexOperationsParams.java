package ro.go.adrhc.persistence.lucene.operations;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexUpsertParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.shallow.TypedIndexShallowUpdaterParams;
import ro.go.adrhc.persistence.lucene.core.typed.search.IndexSearchServiceParams;

import java.io.Closeable;
import java.nio.file.Path;

public interface IndexOperationsParams<T>
	extends TypedIndexRemoverParams, TypedIndexUpsertParams<T>,
	TypedIndexShallowUpdaterParams<T>, Closeable {
	LuceneFieldSpec<T> idField();

	Analyzer analyzer();

	IndexWriter indexWriter();

	Path indexPath();

	boolean isReadOnly();

	IndexSearchServiceParams<T> indexSearchServiceParams();

	DocIndexReaderParams docIndexReaderParams();
}
