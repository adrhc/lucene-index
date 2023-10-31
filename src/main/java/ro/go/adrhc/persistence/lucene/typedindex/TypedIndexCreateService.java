package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.create.DocumentsIndexCreateService;
import ro.go.adrhc.persistence.lucene.index.create.IndexCreateService;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.docserde.TypedToDocumentConverter;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertStream;

@RequiredArgsConstructor
@Slf4j
public class TypedIndexCreateService<T extends Identifiable<?>> implements IndexCreateService<T> {
	private final TypedToDocumentConverter<T> typedToDocumentConverter;
	private final IndexCreateService<Document> documentsIndexCreateService;

	/**
	 * constructor parameters union
	 */
	public static <T extends Identifiable<?>, E extends Enum<E> & TypedField<T>>
	TypedIndexCreateService<T> create(Analyzer analyzer,
			Class<E> typedFieldEnumClass, Path indexPath) {
		return new TypedIndexCreateService<>(
				TypedToDocumentConverter.create(analyzer, typedFieldEnumClass),
				DocumentsIndexCreateService.create(analyzer, indexPath));
	}

	public void createOrReplace(Stream<T> tStream) throws IOException {
		Stream<Document> documents = convertStream(typedToDocumentConverter::convert, tStream);
		documentsIndexCreateService.createOrReplace(documents);
	}
}
