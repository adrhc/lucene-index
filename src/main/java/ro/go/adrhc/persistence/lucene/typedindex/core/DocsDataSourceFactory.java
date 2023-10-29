package ro.go.adrhc.persistence.lucene.typedindex.core;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.core.docds.datasource.DefaultDocsDataSource;
import ro.go.adrhc.persistence.lucene.index.core.docds.datasource.IndexDataSource;
import ro.go.adrhc.persistence.lucene.index.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.index.core.docds.rawds.RawDataSource;
import ro.go.adrhc.persistence.lucene.index.core.docds.rawidserde.RawIdSerde;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedFieldEnum;

import java.util.Collection;

import static ro.go.adrhc.persistence.lucene.index.core.docds.datasource.DefaultDocsDataSource.createCachedDocsDs;

public class DocsDataSourceFactory {
	public static <T extends Identifiable<String>, E extends Enum<E> & TypedFieldEnum<T>>
	IndexDataSource<String, Document> createCachedTypedDs(
			Analyzer analyzer, Class<E> typedFieldEnumClass, Collection<T> tCollection) {
		TypedToDocumentConverter<T> toDocumentConverter =
				TypedToDocumentConverter.create(analyzer, typedFieldEnumClass);
		return createCachedDocsDs(toDocumentConverter::convert, tCollection);
	}

	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedFieldEnum<T>>
	IndexDataSource<String, Document> createTypedDs(Analyzer analyzer,
			Class<E> typedFieldEnumClass, RawIdSerde<ID> rawIdSerde,
			RawDataSource<ID, T> rawDataSource) {
		return new DefaultDocsDataSource<>(rawIdSerde,
				TypedToDocumentConverter.create(analyzer, typedFieldEnumClass),
				rawDataSource);
	}
}
