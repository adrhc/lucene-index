package ro.go.adrhc.persistence.lucene.typedcore.field;

import org.apache.lucene.analysis.Analyzer;

public interface TypedFieldsProviderParams<T, E extends Enum<E> & TypedField<T>> {
	Analyzer getAnalyzer();

	Class<E> getTFieldEnumClass();
}
