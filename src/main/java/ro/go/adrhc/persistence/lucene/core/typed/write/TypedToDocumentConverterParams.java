package ro.go.adrhc.persistence.lucene.core.typed.write;

import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.field.RawFieldValueSerdes;

import java.util.Collection;

public interface TypedToDocumentConverterParams<T> {
	Collection<? extends LuceneFieldSpec<T>> typedFields();

	RawFieldValueSerdes<T> rawFieldValueSerdes();
}
