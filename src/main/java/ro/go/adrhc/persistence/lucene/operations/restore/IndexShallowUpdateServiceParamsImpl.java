package ro.go.adrhc.persistence.lucene.operations.restore;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.field.RawFieldValueSerdes;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemoverParamsImpl;

import java.util.Collection;

import static ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderParamsImpl.allHits;

public record IndexShallowUpdateServiceParamsImpl<T>(Class<T> type, LuceneFieldSpec<T> idField,
	IndexReaderPool indexReaderPool, Collection<? extends LuceneFieldSpec<T>> typedFields,
	Analyzer analyzer, IndexWriter indexWriter, RawFieldValueSerdes<T> rawFieldValueSerdes)
	implements IndexShallowUpdateServiceParams<T> {

	@Override
	public TypedIndexRemoverParams typedIndexRemoverParams() {
		return new TypedIndexRemoverParamsImpl(idField(), indexWriter);
	}

	@Override
	public HitsLimitedIndexReaderParams<T> allHitsTypedIndexReaderParams() {
		return allHits(type(), idField(), indexReaderPool, rawFieldValueSerdes);
	}
}
