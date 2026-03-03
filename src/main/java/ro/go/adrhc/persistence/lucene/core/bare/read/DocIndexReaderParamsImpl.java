package ro.go.adrhc.persistence.lucene.core.bare.read;

import org.apache.lucene.index.IndexReader;

import java.io.IOException;

public record DocIndexReaderParamsImpl(IndexReaderPool indexReaderPool) implements DocIndexReaderParams {
	@Override
	public void closeIndexReader(IndexReader indexReader) throws IOException {
		indexReaderPool.dismissReader(indexReader);
	}

	@Override
	public IndexReader createIndexReader() throws IOException {
		return indexReaderPool.getReader();
	}
}
