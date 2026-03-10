package ro.go.adrhc.persistence.lucene.core.typed.read;

import org.apache.lucene.index.IndexReader;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.field.RawFieldValueSerdes;

import java.io.IOException;

public record TypedIndexReaderParamsImpl<T>(LuceneFieldSpec<T> idField,
	IndexReaderPool indexReaderPool, RawFieldValueSerdes<T> rawFieldValueSerdes)
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
