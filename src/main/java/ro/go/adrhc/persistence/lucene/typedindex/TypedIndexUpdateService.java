package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.IndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.core.docds.datasource.RawToDocumentConverter;
import ro.go.adrhc.persistence.lucene.index.core.docds.rawidserde.RawIdToStringConverter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertCollection;
import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertStream;

@RequiredArgsConstructor
public class TypedIndexUpdateService<ID, T> {
	private final RawIdToStringConverter<ID> toStringConverter;
	private final RawToDocumentConverter<T> toDocumentConverter;
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
		List<Document> documents = convertCollection(toDocumentConverter::convert, tCollection);
		indexUpdateService.addDocuments(documents);
		return documents.size();
	}

	public void addAll(Stream<T> tCollection) throws IOException {
		Stream<Document> documents = convertStream(toDocumentConverter::convert, tCollection);
		indexUpdateService.addDocuments(documents);
	}

	public int removeByIds(Collection<ID> ids) throws IOException {
		List<String> docIds = convertCollection(toStringConverter::convert, ids);
		indexUpdateService.removeByIds(docIds);
		return docIds.size();
	}
}
