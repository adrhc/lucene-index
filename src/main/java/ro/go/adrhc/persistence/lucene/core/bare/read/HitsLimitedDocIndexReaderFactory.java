package ro.go.adrhc.persistence.lucene.core.bare.read;

import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class HitsLimitedDocIndexReaderFactory {
	public static HitsLimitedDocIndexReader
	allHits(IndexReaderPool indexReaderPool) throws IOException {
		return create(indexReaderPool, Integer.MAX_VALUE);
	}

	public static HitsLimitedDocIndexReader create(
		IndexReaderPool indexReaderPool, int numHits) throws IOException {
		return new HitsLimitedDocIndexReader(
			indexReaderPool::dismissReader, indexReaderPool.getReader(), numHits);
	}

	public static HitsLimitedDocIndexReader create(
		DocIndexReaderParams params, int numHits) throws IOException {
		return new HitsLimitedDocIndexReader(params::closeIndexReader,
			params.createIndexReader(), numHits);
	}

	public static HitsLimitedDocIndexReader create(
		HitsLimitedDocIndexReaderParams params) throws IOException {
		return new HitsLimitedDocIndexReader(params::closeIndexReader,
			params.createIndexReader(), params.numHits());
	}
}
