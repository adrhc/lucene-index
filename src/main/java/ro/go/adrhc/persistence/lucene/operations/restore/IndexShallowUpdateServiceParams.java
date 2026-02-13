package ro.go.adrhc.persistence.lucene.operations.restore;

import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexWriterParams;

public interface IndexShallowUpdateServiceParams<T> extends TypedIndexWriterParams<T> {
	IndexReaderPool getIndexReaderPool();

	HitsLimitedIndexReaderParams<T> allHitsTypedIndexReaderParams();

	IndexWriter indexWriter();

	TypedIndexRemoverParams typedIndexRemoverParams();
}
