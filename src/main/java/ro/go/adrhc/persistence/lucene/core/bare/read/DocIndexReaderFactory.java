package ro.go.adrhc.persistence.lucene.core.bare.read;

import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class DocIndexReaderFactory {
	public static DocIndexReader of(DocIndexReaderParams params) throws IOException {
		return new DocIndexReader(params::closeIndexReader, params.createIndexReader());
	}

	public static DocIndexReader of(IndexReaderPool indexReaderPool) throws IOException {
		return new DocIndexReader(indexReaderPool::dismissReader, indexReaderPool.getReader());
	}
}
