package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexCreateService;
import ro.go.adrhc.persistence.lucene.index.IndexCreateService;
import ro.go.adrhc.persistence.lucene.index.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.core.TypedToDocumentConverter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertStream;

@RequiredArgsConstructor
@Slf4j
public class FSTypedIndexCreateService<ID, T extends Identifiable<ID>> implements IndexCreateService<T> {
	private final TypedToDocumentConverter<T> toDocumentConverter;
	private final FSIndexCreateService fsIndexCreateService;

	public static <ID, T extends Identifiable<ID>> FSTypedIndexCreateService<ID, T> create(
			TypedToDocumentConverter<T> toDocumentConverter, Analyzer analyzer, Path indexPath) {
		return new FSTypedIndexCreateService<>(toDocumentConverter,
				FSIndexCreateService.create(analyzer, indexPath));
	}

	public void createOrReplace(Stream<T> tStream) throws IOException {
		Stream<Document> documents = convertStream(toDocumentConverter::convert, tStream);
		fsIndexCreateService.createOrReplace(documents);
	}
}
