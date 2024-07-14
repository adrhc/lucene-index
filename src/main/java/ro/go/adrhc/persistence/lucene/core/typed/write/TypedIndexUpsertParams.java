package ro.go.adrhc.persistence.lucene.core.typed.write;

import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

public interface TypedIndexUpsertParams<T> extends TypedIndexWriterParams<T> {
	LuceneFieldSpec<T> getIdField();
}
