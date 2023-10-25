package ro.go.adrhc.persistence.lucene.typedindex.core;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.domain.DocumentsDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.domain.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.rawds.RawDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.domain.rawidserde.RawIdSerde;
import ro.go.adrhc.persistence.lucene.typedindex.domain.rawtodoc.RawToDocumentConverter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertAll;

@RequiredArgsConstructor
public class DefaultDocumentsDataSource<ID, T extends Identifiable<ID>> implements DocumentsDataSource {
	private final RawDataSource<ID, T> rawDataDatasource;
	private final RawIdSerde<ID> rawIdSerde;
	private final RawToDocumentConverter<T> toDocumentConverter;

	@Override
	public List<String> loadAllIds() throws IOException {
		return rawDataDatasource.loadAllIds().stream()
				.map(rawIdSerde::toString)
				.flatMap(Optional::stream)
				.toList();
	}

	@Override
	public List<Document> loadByIds(Collection<String> ids) throws IOException {
		Collection<ID> rawIds = convertAll(rawIdSerde::toId, ids);
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
