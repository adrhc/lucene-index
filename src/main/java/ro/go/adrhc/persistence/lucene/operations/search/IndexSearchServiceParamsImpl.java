package ro.go.adrhc.persistence.lucene.operations.search;

import org.apache.lucene.index.IndexReader;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.field.RawFieldValueSerdes;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParamsImpl;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderParamsImpl;

import java.io.IOException;

public record IndexSearchServiceParamsImpl<T>(Class<T> type, LuceneFieldSpec<T> idField,
	IndexReaderPool indexReaderPool, RawFieldValueSerdes<T> rawFieldValueSerdes,
	SearchResultFilter<T> searchResultFilter, int numHits)
	implements IndexSearchServiceParams<T> {
	@Override
	public void closeIndexReader(IndexReader indexReader) throws IOException {
		indexReaderPool.dismissReader(indexReader);
	}

	@Override
	public IndexReader createIndexReader() throws IOException {
		return indexReaderPool.getReader();
	}

	@Override
	public TypedIndexReaderParams<T> typedIndexReaderParams() {
		return new TypedIndexReaderParamsImpl<>(type(), indexReaderPool, rawFieldValueSerdes);
	}

	@Override
	public HitsLimitedIndexReaderParams<T> allHitsTypedIndexReaderParams() {
		return HitsLimitedIndexReaderParamsImpl.allHits(
			type, idField, indexReaderPool, rawFieldValueSerdes);
	}
}
