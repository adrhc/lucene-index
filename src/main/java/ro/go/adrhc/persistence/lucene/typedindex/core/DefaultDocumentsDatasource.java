package ro.go.adrhc.persistence.lucene.typedindex.core;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.spi.DocumentsDatasource;
import ro.go.adrhc.persistence.lucene.typedindex.spi.RawDataDatasource;
import ro.go.adrhc.persistence.lucene.typedindex.spi.RawDataIdToStringConverter;
import ro.go.adrhc.persistence.lucene.typedindex.spi.RawDataToDocumentConverter;
import ro.go.adrhc.persistence.lucene.typedindex.spi.StringToRawDataIdConverter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertAll;

@RequiredArgsConstructor
public class DefaultDocumentsDatasource<ID, T> implements DocumentsDatasource {
	private final RawDataDatasource<ID, T> rawDataDatasource;
	private final RawDataIdToStringConverter<ID> rawDataIdToStringConverter;
	private final StringToRawDataIdConverter<ID> stringToRawDataIdConverter;
	private final RawDataToDocumentConverter<T> toDocumentConverter;

	@Override
	public List<String> loadAllIds() throws IOException {
		return rawDataDatasource.loadAllIds().stream()
				.map(rawDataIdToStringConverter::convert)
				.flatMap(Optional::stream)
				.toList();
	}

	@Override
	public List<Document> loadByIds(Collection<String> ids) throws IOException {
		Collection<ID> rawIds = convertAll(stringToRawDataIdConverter::convert, ids);
		return rawDataDatasource.loadByIds(rawIds).stream()
				.map(toDocumentConverter::convert)
				.flatMap(Optional::stream)
				.toList();
	}

	@Override
	public List<Document> loadAll() throws IOException {
		return rawDataDatasource.loadAll().stream()
				.map(toDocumentConverter::convert)
				.flatMap(Optional::stream)
				.toList();
	}
}
