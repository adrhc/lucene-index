package ro.go.adrhc.persistence.lucene.typedcore.write;

import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;

public interface TypedIndexUpsertParams<T> extends TypedIndexWriterParams<T> {
	LuceneFieldSpec<T> getIdField();
}
