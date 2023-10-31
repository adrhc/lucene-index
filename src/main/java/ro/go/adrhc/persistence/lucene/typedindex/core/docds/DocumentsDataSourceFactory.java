package ro.go.adrhc.persistence.lucene.typedindex.core.docds;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.RawDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawidserde.RawIdSerde;
import ro.go.adrhc.persistence.lucene.typedindex.domain.docserde.TypedToDocumentConverter;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.util.Collection;

import static ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.RawDataSourceFactories.createCachedRawDs;
import static ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawidserde.RawIdSerdeFactory.STRING_RAW_ID_SERDE;

public class DocumentsDataSourceFactory {
	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
	IndexDataSource<String, Document> create(Analyzer analyzer,
			Class<E> typedFieldEnumClass, RawIdSerde<ID> rawIdSerde,
			RawDataSource<ID, T> rawDataSource) {
		return new DocumentsDataSource<>(rawIdSerde,
				TypedToDocumentConverter.create(analyzer, typedFieldEnumClass),
				rawDataSource);
	}

	public static <T extends Identifiable<String>, E extends Enum<E> & TypedField<T>>
	IndexDataSource<String, Document> createCached(
			Analyzer analyzer, Class<E> typedFieldEnumClass, Collection<T> tCollection) {
		TypedToDocumentConverter<T> toDocumentConverter =
				TypedToDocumentConverter.create(analyzer, typedFieldEnumClass);
		return new DocumentsDataSource<>(STRING_RAW_ID_SERDE,
				toDocumentConverter, createCachedRawDs(tCollection));
	}
}
