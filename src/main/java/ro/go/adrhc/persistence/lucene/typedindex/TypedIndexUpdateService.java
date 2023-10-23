package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.IndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.spi.RawDataIdToStringConverter;
import ro.go.adrhc.persistence.lucene.typedindex.spi.RawDataToDocumentConverter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertAll;

@RequiredArgsConstructor
public class TypedIndexUpdateService<ID, T> {
	private final RawDataIdToStringConverter<ID> toStringConverter;
	private final RawDataToDocumentConverter<T> toDocumentConverter;
	private final IndexUpdateService indexUpdateService;

	public boolean add(T t) throws IOException {
		Optional<Document> optionalDocument = toDocumentConverter.convert(t);
		if (optionalDocument.isEmpty()) {
			return false;
		}
		indexUpdateService.addDocument(optionalDocument.get());
		return true;
	}

	public int addAll(Collection<T> tCollection) throws IOException {
		List<Document> documents = convertAll(toDocumentConverter::convert, tCollection);
		indexUpdateService.addDocuments(documents);
		return documents.size();
	}

	public int removeByIds(Collection<ID> ids) throws IOException {
		List<String> docIds = convertAll(toStringConverter::convert, ids);
		indexUpdateService.removeByIds(docIds);
		return docIds.size();
	}
}
