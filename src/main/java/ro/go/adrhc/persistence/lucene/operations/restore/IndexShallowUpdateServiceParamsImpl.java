package ro.go.adrhc.persistence.lucene.operations.restore;

import lombok.Getter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.read.AllHitsTypedIndexReaderParamsFactory;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemoverParamsImpl;

import java.util.Collection;

@Getter
public class IndexShallowUpdateServiceParamsImpl<T>
		extends AllHitsTypedIndexReaderParamsFactory<T>
		implements IndexShallowUpdateServiceParams<T> {
	private final Collection<? extends LuceneFieldSpec<T>> typedFields;
	private final Analyzer analyzer;
	private final IndexWriter indexWriter;

	public IndexShallowUpdateServiceParamsImpl(Class<T> type,
			LuceneFieldSpec<T> idField, IndexReaderPool indexReaderPool,
			Collection<? extends LuceneFieldSpec<T>> typedFields,
			Analyzer analyzer, IndexWriter indexWriter) {
		super(type, idField, indexReaderPool);
		this.analyzer = analyzer;
		this.typedFields = typedFields;
		this.indexWriter = indexWriter;
	}

	@Override
	public TypedIndexRemoverParams typedIndexRemoverParams() {
		return new TypedIndexRemoverParamsImpl(getIdField(), indexWriter);
	}
}
