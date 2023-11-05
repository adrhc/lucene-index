package ro.go.adrhc.persistence.lucene.typedindex.core.docds;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.RawDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.domain.docserde.TypedToDocumentConverter;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DocumentsDataSource<ID, T extends Identifiable<ID>> implements IndexDataSource<ID, Document> {
	private final TypedToDocumentConverter<T> typedToDocumentConverter;
	private final RawDataSource<ID, T> rawDataSource;

	@Override
	public Stream<ID> loadAllIds() throws IOException {
		return rawDataSource.loadAllIds();
	}

	@Override
	public Stream<Document> loadByIds(Stream<ID> ids) throws IOException {
		return rawDataSource.loadByIds(ids)
				.map(typedToDocumentConverter::convert)
				.flatMap(Optional::stream);
	}
}
