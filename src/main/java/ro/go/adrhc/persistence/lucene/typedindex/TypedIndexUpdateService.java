package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.update.DocumentsIndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.update.IndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawidserde.RawIdToStringConverter;
import ro.go.adrhc.persistence.lucene.typedindex.domain.docserde.TypedToDocumentConverter;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedFieldEnum;
import ro.go.adrhc.util.Assert;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedFieldEnum.getIdField;
import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertCollection;
import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertStream;

@RequiredArgsConstructor
public class TypedIndexUpdateService<ID, T extends Identifiable<?>> implements IndexUpdateService<ID, T> {
	private final RawIdToStringConverter<ID> rawIdToStringConverter;
	private final TypedToDocumentConverter<T> typedToDocumentConverter;
	private final IndexUpdateService<String, Document> indexUpdateService;

	/**
	 * constructor parameters union
	 * <p>
	 * rawIdToStringConverter: Object::toString
	 */
	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedFieldEnum<T>>
	TypedIndexUpdateService<ID, T> create(
			Analyzer analyzer, Class<E> tFieldEnumClass, Path indexPath) {
		return new TypedIndexUpdateService<>(
				RawIdToStringConverter.of(Object::toString),
				TypedToDocumentConverter.create(analyzer, tFieldEnumClass),
				DocumentsIndexUpdateService.create(
						getIdField(tFieldEnumClass), analyzer, indexPath));
	}

	public void add(T t) throws IOException {
		Optional<Document> optionalDocument = typedToDocumentConverter.convert(t);
		Assert.isTrue(optionalDocument.isPresent(), "Typed to document conversion failed!");
		indexUpdateService.add(optionalDocument.get());
	}

	public void addAll(Collection<T> tCollection) throws IOException {
		List<Document> documents = convertCollection(typedToDocumentConverter::convert, tCollection);
		Assert.isTrue(tCollection.size() == documents.size(), "Typed to document conversion failed!");
		indexUpdateService.addAll(documents);
	}

	public void addAll(Stream<T> tCollection) throws IOException {
		Stream<Document> documents = convertStream(typedToDocumentConverter::convert, tCollection);
		indexUpdateService.addAll(documents);
	}

	public void removeByIds(Collection<ID> ids) throws IOException {
		List<String> docIds = convertCollection(rawIdToStringConverter::convert, ids);
		Assert.isTrue(ids.size() == docIds.size(), "Raw id to String conversion failed!");
		indexUpdateService.removeByIds(docIds);
	}
}
