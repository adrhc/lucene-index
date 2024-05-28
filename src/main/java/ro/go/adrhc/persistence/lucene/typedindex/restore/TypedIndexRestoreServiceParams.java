package ro.go.adrhc.persistence.lucene.typedindex.restore;

import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexWriterParams;

public interface TypedIndexRestoreServiceParams<T> extends TypedIndexWriterParams<T> {
	IndexReaderPool getIndexReaderPool();

	TypedIndexReaderParams<T> toAllHitsTypedIndexReaderParams();

	IndexWriter getIndexWriter();

	TypedIndexRemoverParams toTypedIndexRemoverParams();
}
