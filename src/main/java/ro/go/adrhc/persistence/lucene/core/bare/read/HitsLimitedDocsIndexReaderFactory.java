package ro.go.adrhc.persistence.lucene.core.bare.read;

import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class HitsLimitedDocsIndexReaderFactory {
	public static HitsLimitedDocsIndexReader
	createUnlimited(IndexReaderPool indexReaderPool) throws IOException {
		return new HitsLimitedDocsIndexReader(indexReaderPool,
			indexReaderPool.getReader(), Integer.MAX_VALUE);
	}

	public static HitsLimitedDocsIndexReader create(
		HitsLimitedDocsIndexReaderParams params) throws IOException {
		return new HitsLimitedDocsIndexReader(params.indexReaderPool(),
			params.indexReaderPool().getReader(), params.numHits());
	}

	public static HitsLimitedDocsIndexReader create(
		IndexReaderPool indexReaderPool, int numHits) throws IOException {
		return new HitsLimitedDocsIndexReader(
			indexReaderPool, indexReaderPool.getReader(), numHits);
	}
}
