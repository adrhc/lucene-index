package ro.go.adrhc.persistence.lucene.typedindex.core.docds;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.RawDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawidserde.RawIdSerde;
import ro.go.adrhc.persistence.lucene.typedindex.domain.docserde.TypedToDocumentConverter;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertStream;

@RequiredArgsConstructor
public class DocumentsDataSource<ID, T extends Identifiable<ID>> implements IndexDataSource<String, Document> {
	private final RawIdSerde<ID> rawIdSerde;
	private final TypedToDocumentConverter<T> typedToDocumentConverter;
	private final RawDataSource<ID, T> rawDataSource;

	@Override
	public Stream<String> loadAllIds() throws IOException {
		return rawDataSource.loadAllIds()
				.map(rawIdSerde::rawIdToString)
				.flatMap(Optional::stream);
	}

	@Override
	public Stream<Document> loadByIds(Stream<String> ids) throws IOException {
		Stream<ID> rawIds = convertStream(rawIdSerde::stringToRawId, ids);
		return rawDataSource.loadByIds(rawIds)
				.map(typedToDocumentConverter::convert)
				.flatMap(Optional::stream);
	}
}
