package ro.go.adrhc.persistence.lucene.typedindex.core;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentsIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedindex.domain.docserde.DocumentToTypedConverter;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static ro.go.adrhc.util.ObjectUtils.cast;
import static ro.go.adrhc.util.fn.SneakyBiFunctionUtils.curry;

@RequiredArgsConstructor
public class TypedIndexReaderTemplate<T> {
	private final DocumentToTypedConverter<T> docToTypedConverter;
	private final DocumentsIndexReaderTemplate docIndexReaderTemplate;

	/**
	 * constructor parameters union
	 * <p>
	 * numHits = Integer.MAX_VALUE
	 */
	public static <T> TypedIndexReaderTemplate<T> create(Class<T> tClass, Path indexPath) {
		return new TypedIndexReaderTemplate<>(
				DocumentToTypedConverter.of(tClass),
				DocumentsIndexReaderTemplate.create(indexPath));
	}

	/**
	 * constructor parameters union
	 */
	public static <T> TypedIndexReaderTemplate<T> create(
			Class<T> tClass, int numHits, Path indexPath) {
		return new TypedIndexReaderTemplate<>(
				DocumentToTypedConverter.of(tClass),
				new DocumentsIndexReaderTemplate(numHits, indexPath));
	}

	public <R> R transform(Function<Stream<T>, R> transformer) throws IOException {
		return docIndexReaderTemplate.transformDocuments(curry(this::doTransform, transformer));
	}

	public <R> R transform(Set<String> fieldNames, Function<Stream<T>, R> transformer) throws IOException {
		return docIndexReaderTemplate.transformDocuments(fieldNames, curry(this::doTransform, transformer));
	}

	public <V, R> R transformFieldValues(TypedField<?> typedField,
			SneakyFunction<Stream<V>, R, IOException> transformer) throws IOException {
		return docIndexReaderTemplate.transformFields(typedField.name(),
				fields -> transformer.apply(fields.map(cast(typedField.fieldValueAccessor()))));
	}

	public <R> R transformFieldValues(String fieldName,
			SneakyFunction<Stream<String>, R, IOException> transformer) throws IOException {
		return docIndexReaderTemplate.transformFieldValues(fieldName, transformer);
	}

	private <R> R doTransform(
			Function<Stream<T>, R> transformer, Stream<Document> documents) {
		return transformer.apply(documents.map(docToTypedConverter::convert).flatMap(Optional::stream));
	}
}
