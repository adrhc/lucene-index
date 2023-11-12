package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.update.DocumentsIndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.update.IndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedcore.docserde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedcore.docserde.TypedToDocumentConverter;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactoriesParams;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertCollection;
import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertStream;

@RequiredArgsConstructor
public class TypedIndexUpdateService<T extends Identifiable<?>> implements IndexUpdateService<T> {
	private final TypedToDocumentConverter<T> typedToDocumentConverter;
	private final IndexUpdateService<Document> indexUpdateService;

	/**
	 * constructor parameters union
	 * <p>
	 * rawIdToStringConverter: Object::toString
	 */
	public static <T extends Identifiable<?>, E extends Enum<E> & TypedField<T>>
	TypedIndexUpdateService<T> create(TypedIndexFactoriesParams<?, T, E> factoriesParams) {
		return new TypedIndexUpdateService<>(
				TypedToDocumentConverter.create(factoriesParams),
				DocumentsIndexUpdateService.create(factoriesParams));
	}

	public void add(T t) throws IOException {
		Optional<Document> optionalDocument = typedToDocumentConverter.convert(t);
		if (optionalDocument.isPresent()) {
			indexUpdateService.add(optionalDocument.get());
		}
	}

	public void addAll(Collection<T> tCollection) throws IOException {
		List<Document> documents = convertCollection(typedToDocumentConverter::convert, tCollection);
		indexUpdateService.addAll(documents);
	}

	public void addAll(Stream<T> tCollection) throws IOException {
		Stream<Document> documents = convertStream(typedToDocumentConverter::convert, tCollection);
		indexUpdateService.addAll(documents);
	}

	@Override
	public void update(T t) throws IOException {
		Optional<Document> optionalDocument = typedToDocumentConverter.convert(t);
		if (optionalDocument.isPresent()) {
			indexUpdateService.update(optionalDocument.get());
		}
	}
}
