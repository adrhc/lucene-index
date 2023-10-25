package ro.go.adrhc.persistence.lucene.typedindex.core;

import org.apache.lucene.analysis.Analyzer;
import ro.go.adrhc.persistence.lucene.index.core.docds.DocumentsDataSource;
import ro.go.adrhc.persistence.lucene.index.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedFieldEnum;

import java.util.Collection;

import static ro.go.adrhc.persistence.lucene.index.core.docds.DefaultDocsDataSource.createCachedDocsDs;

public class DocsDataSourceFactory {
	public static <T extends Identifiable<String>, E extends Enum<E> & TypedFieldEnum<T>>
	DocumentsDataSource createCachedTypedDocsDs(
			Analyzer analyzer, Class<E> typedFieldEnumClass, Collection<T> tCollection) {
		TypedToDocumentConverter<T> toDocumentConverter =
				TypedToDocumentConverter.create(analyzer, typedFieldEnumClass);
		return createCachedDocsDs(toDocumentConverter::convert, tCollection);
	}
}
