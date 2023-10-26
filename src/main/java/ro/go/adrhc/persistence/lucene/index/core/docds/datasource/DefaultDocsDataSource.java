package ro.go.adrhc.persistence.lucene.index.core.docds.datasource;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.index.core.docds.rawds.RawDataSource;
import ro.go.adrhc.persistence.lucene.index.core.docds.rawidserde.RawIdSerde;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.index.core.docds.rawds.RawDataSourceFactories.createCachedRawDs;
import static ro.go.adrhc.persistence.lucene.index.core.docds.rawidserde.RawIdSerdeFactory.STRING_RAW_ID_SERDE;
import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertAll;

@RequiredArgsConstructor
public class DefaultDocsDataSource<ID, T extends Identifiable<ID>> implements DocumentsDataSource {
	private final RawDataSource<ID, T> rawDataSource;
	private final RawIdSerde<ID> rawIdSerde;
	private final RawToDocumentConverter<T> rawToDocumentConverter;

	public static <T extends Identifiable<String>> DocumentsDataSource createCachedDocsDs(
			Function<T, Optional<Document>> toDocumentConverter, Collection<T> tCollection) {
		return new DefaultDocsDataSource<>(
				createCachedRawDs(tCollection),
				STRING_RAW_ID_SERDE,
				toDocumentConverter::apply
		);
	}

	@Override
	public List<String> loadAllIds() throws IOException {
		return rawDataSource.loadAllIds().stream()
				.map(rawIdSerde::toString)
				.flatMap(Optional::stream)
				.toList();
	}

	@Override
	public List<Document> loadByIds(Collection<String> ids) throws IOException {
		Collection<ID> rawIds = convertAll(rawIdSerde::toId, ids);
		return rawDataSource.loadByIds(rawIds).stream()
				.map(rawToDocumentConverter::convert)
				.flatMap(Optional::stream)
				.toList();
	}

	@Override
	public List<Document> loadAll() throws IOException {
		return rawDataSource.loadAll().stream()
				.map(rawToDocumentConverter::convert)
				.flatMap(Optional::stream)
				.toList();
	}
}
