package ro.go.adrhc.persistence.lucene.typedcore.field;

import org.apache.lucene.analysis.Analyzer;

import java.util.Collection;

public interface TypedFieldsProviderParams<T> {
	Analyzer getAnalyzer();

	Collection<? extends TypedField<T>> getTypedFields();
}
