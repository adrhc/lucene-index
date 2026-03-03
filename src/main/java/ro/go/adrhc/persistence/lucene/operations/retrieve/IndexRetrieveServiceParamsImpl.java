package ro.go.adrhc.persistence.lucene.operations.retrieve;

import org.apache.lucene.index.IndexReader;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParamsImpl;

import java.io.IOException;

public record IndexRetrieveServiceParamsImpl<T>(Class<T> type, LuceneFieldSpec<T> idField,
	IndexReaderPool indexReaderPool) implements IndexRetrieveServiceParams<T> {
	@Override
	public HitsLimitedIndexReaderParams<T> allHitsIndexReaderParams() {
		return HitsLimitedIndexReaderParamsImpl.allHits(type(), idField(), indexReaderPool);
	}

	@Override
	public void closeIndexReader(IndexReader indexReader) throws IOException {
		indexReaderPool.dismissReader(indexReader);
	}

	@Override
	public IndexReader createIndexReader() throws IOException {
		return indexReaderPool.getReader();
	}
}
