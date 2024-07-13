package ro.go.adrhc.persistence.lucene.typedindex.restore;

import lombok.Getter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.typedcore.write.DefaultTypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.typedindex.AllHitsTypedIndexReaderParams;

import java.util.Collection;

@Getter
public class DefaultTypedShallowUpdateServiceParams<T>
		extends AllHitsTypedIndexReaderParams<T>
		implements TypedShallowUpdateServiceParams<T> {
	private final Collection<? extends LuceneFieldSpec<T>> typedFields;
	private final Analyzer analyzer;
	private final IndexWriter indexWriter;

	public DefaultTypedShallowUpdateServiceParams(Class<T> type,
			LuceneFieldSpec<T> idField, IndexReaderPool indexReaderPool,
			Collection<? extends LuceneFieldSpec<T>> typedFields,
			Analyzer analyzer, IndexWriter indexWriter) {
		super(type, idField, indexReaderPool);
		this.analyzer = analyzer;
		this.typedFields = typedFields;
		this.indexWriter = indexWriter;
	}

	@Override
	public TypedIndexRemoverParams toTypedIndexRemoverParams() {
		return new DefaultTypedIndexRemoverParams(getIdField(), indexWriter);
	}
}
