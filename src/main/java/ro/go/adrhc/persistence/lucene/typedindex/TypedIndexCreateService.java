package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.create.DocumentsIndexCreateService;
import ro.go.adrhc.persistence.lucene.index.create.IndexCreateService;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedcore.serde.TypedToDocumentConverter;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactoriesParams;

import java.io.IOException;
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
	TypedIndexCreateService<T> create(TypedIndexFactoriesParams<?, T, E> factoriesParams) {
		return new TypedIndexCreateService<>(
				TypedToDocumentConverter.create(factoriesParams),
				DocumentsIndexCreateService.create(factoriesParams));
	}

	public void createOrReplace(Stream<T> tStream) throws IOException {
		Stream<Document> documents = convertStream(typedToDocumentConverter::convert, tStream);
		documentsIndexCreateService.createOrReplace(documents);
	}
}
