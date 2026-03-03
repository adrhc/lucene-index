package ro.go.adrhc.persistence.lucene.core.typed.read;

import org.apache.lucene.index.IndexReader;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;

import java.io.IOException;

public record TypedIndexReaderParamsImpl<T>(Class<T> type, IndexReaderPool indexReaderPool)
	implements TypedIndexReaderParams<T> {
	@Override
	public void closeIndexReader(IndexReader indexReader) throws IOException {
		indexReaderPool.dismissReader(indexReader);
	}

	@Override
	public IndexReader createIndexReader() throws IOException {
		return indexReaderPool.getReader();
	}
}
