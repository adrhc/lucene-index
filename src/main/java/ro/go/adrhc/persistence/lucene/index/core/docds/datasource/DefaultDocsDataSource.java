package ro.go.adrhc.persistence.lucene.index.core.docds.datasource;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.index.core.docds.rawds.RawDataSource;
import ro.go.adrhc.persistence.lucene.index.core.docds.rawidserde.RawIdSerde;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static ro.go.adrhc.persistence.lucene.index.core.docds.rawds.RawDataSourceFactories.createCachedRawDs;
import static ro.go.adrhc.persistence.lucene.index.core.docds.rawidserde.RawIdSerdeFactory.STRING_RAW_ID_SERDE;
import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertStream;

@RequiredArgsConstructor
public class DefaultDocsDataSource<ID, T extends Identifiable<ID>> implements IndexDataSource<String, Document> {
	private final RawIdSerde<ID> rawIdSerde;
	private final RawToDocumentConverter<T> rawToDocumentConverter;
	private final RawDataSource<ID, T> rawDataSource;

	public static <T extends Identifiable<String>>
	IndexDataSource<String, Document> createCachedDocsDs(
			Function<T, Optional<Document>> toDocumentConverter, Collection<T> tCollection) {
		return new DefaultDocsDataSource<>(STRING_RAW_ID_SERDE,
				toDocumentConverter::apply, createCachedRawDs(tCollection));
	}

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
				.map(rawToDocumentConverter::convert)
				.flatMap(Optional::stream);
	}
}
