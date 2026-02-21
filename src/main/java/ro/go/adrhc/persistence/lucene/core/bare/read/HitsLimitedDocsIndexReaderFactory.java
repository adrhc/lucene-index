package ro.go.adrhc.persistence.lucene.core.bare.read;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Path;

@UtilityClass
public class HitsLimitedDocsIndexReaderFactory {
	public static HitsLimitedDocsIndexReader createUnlimited(Path indexPath) throws IOException {
		return createUnlimited(IndexReaderPoolFactory.of(indexPath));
	}

	public static HitsLimitedDocsIndexReader
	createUnlimited(IndexReaderPool indexReaderPool) throws IOException {
		return create(indexReaderPool, Integer.MAX_VALUE);
	}

	public static HitsLimitedDocsIndexReader create(
		HitsLimitedDocsIndexReaderParams params) throws IOException {
		return create(params.indexReaderPool(), params.numHits());
	}

	public static HitsLimitedDocsIndexReader create(
		IndexReaderPool indexReaderPool, int numHits) throws IOException {
		return new HitsLimitedDocsIndexReader(
			indexReaderPool, indexReaderPool.getReader(), numHits);
	}
}
