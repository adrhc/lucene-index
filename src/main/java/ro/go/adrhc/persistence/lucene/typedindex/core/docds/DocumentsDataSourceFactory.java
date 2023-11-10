package ro.go.adrhc.persistence.lucene.typedindex.core.docds;

import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexSpec;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.RawDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.domain.docserde.TypedToDocumentConverter;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;

import java.util.Collection;

import static ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.RawDataSourceFactories.createCachedRawDs;

public class DocumentsDataSourceFactory {
	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
	IndexDataSource<ID, Document> create(
			TypedIndexSpec<ID, T, E> typedIndexSpec, RawDataSource<ID, T> rawDataSource) {
		return new DocumentsDataSource<>(
				TypedToDocumentConverter.create(typedIndexSpec), rawDataSource);
	}

	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
	IndexDataSource<ID, Document> createCached(
			TypedIndexSpec<ID, T, E> typedIndexSpec, Collection<T> tCollection) {
		TypedToDocumentConverter<T> toDocumentConverter =
				TypedToDocumentConverter.create(typedIndexSpec);
		return new DocumentsDataSource<>(toDocumentConverter, createCachedRawDs(tCollection));
	}
}
