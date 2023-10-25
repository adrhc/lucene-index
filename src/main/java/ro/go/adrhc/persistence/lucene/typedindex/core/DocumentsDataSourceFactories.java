package ro.go.adrhc.persistence.lucene.typedindex.core;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.core.DocumentsDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.core.rawds.RawDataSourceFactories;
import ro.go.adrhc.persistence.lucene.typedindex.core.rawtodoc.TypedDataToDocumentConverter;
import ro.go.adrhc.persistence.lucene.typedindex.domain.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedFieldEnum;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.typedindex.core.rawidserde.RawIdSerde.createStringRawIdSerde;

public class DocumentsDataSourceFactories {
	public static <T extends Identifiable<String>, E extends Enum<E> & TypedFieldEnum<T>>
	DocumentsDataSource createCachedTypedDocsDs(
			Analyzer analyzer, Class<E> typedFieldEnumClass, Collection<T> tCollection) {
		TypedDataToDocumentConverter<T> toDocumentConverter =
				TypedDataToDocumentConverter.create(analyzer, typedFieldEnumClass);
		return createCachedDocsDs(toDocumentConverter::convert, tCollection);
	}

	public static <T extends Identifiable<String>> DocumentsDataSource createCachedDocsDs(
			Function<T, Optional<Document>> toDocumentConverter, Collection<T> tCollection) {
		return new DefaultDocumentsDataSource<>(
				RawDataSourceFactories.create(tCollection),
				createStringRawIdSerde(),
				toDocumentConverter::apply
		);
	}
}
