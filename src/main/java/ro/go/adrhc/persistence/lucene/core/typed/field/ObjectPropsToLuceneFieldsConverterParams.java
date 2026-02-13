package ro.go.adrhc.persistence.lucene.core.typed.field;

import org.apache.lucene.analysis.Analyzer;

import java.util.Collection;

public interface ObjectPropsToLuceneFieldsConverterParams<T> {
	Analyzer getAnalyzer();

	Collection<? extends LuceneFieldSpec<T>> getTypedFields();
}
