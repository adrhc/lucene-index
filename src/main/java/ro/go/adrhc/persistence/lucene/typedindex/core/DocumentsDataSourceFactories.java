package ro.go.adrhc.persistence.lucene.typedindex.core;

import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.domain.DocumentsDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.domain.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.rawds.RawDataSourceFactories;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.typedindex.domain.rawidserde.RawIdSerde.createStringRawIdSerde;

public class DocumentsDataSourceFactories {
	public static <T extends Identifiable<String>> DocumentsDataSource createCachedDocsDs(
			Function<T, Optional<Document>> toDocumentConverter, Collection<T> tCollection) {
		return new DefaultDocumentsDataSource<>(
				RawDataSourceFactories.createRawDs(tCollection),
				createStringRawIdSerde(),
				toDocumentConverter::apply
		);
	}
}
