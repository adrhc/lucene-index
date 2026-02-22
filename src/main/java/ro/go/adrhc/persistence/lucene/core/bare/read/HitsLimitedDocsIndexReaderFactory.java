package ro.go.adrhc.persistence.lucene.core.bare.read;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Path;

@UtilityClass
public class HitsLimitedDocsIndexReaderFactory {
	public static HitsLimitedDocIndexReader createUnlimited(Path indexPath) throws IOException {
		return createUnlimited(IndexReaderPoolFactory.of(indexPath));
	}

	public static HitsLimitedDocIndexReader
	createUnlimited(IndexReaderPool indexReaderPool) throws IOException {
		return create(indexReaderPool, Integer.MAX_VALUE);
	}

	public static HitsLimitedDocIndexReader create(
		HitsLimitedDocsIndexReaderParams params) throws IOException {
		return create(params.indexReaderPool(), params.numHits());
	}

	public static HitsLimitedDocIndexReader create(
		IndexReaderPool indexReaderPool, int numHits) throws IOException {
		return new HitsLimitedDocIndexReader(
			indexReaderPool, indexReaderPool.getReader(), numHits);
	}
}
