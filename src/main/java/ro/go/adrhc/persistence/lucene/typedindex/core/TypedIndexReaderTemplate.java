package ro.go.adrhc.persistence.lucene.typedindex.core;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedindex.domain.docserde.DocumentToTypedConverter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static ro.go.adrhc.util.fn.SneakyBiFunctionUtils.curry;

@RequiredArgsConstructor
public class TypedIndexReaderTemplate<T> {
	private final DocumentToTypedConverter<T> docToTypedConverter;
	private final DocumentIndexReaderTemplate docIndexReaderTemplate;

	/**
	 * constructor parameters union
	 * <p>
	 * numHits = Integer.MAX_VALUE
	 */
	public static <T> TypedIndexReaderTemplate<T> create(Class<T> tClass, Path indexPath) {
		return new TypedIndexReaderTemplate<>(
				DocumentToTypedConverter.of(tClass),
				DocumentIndexReaderTemplate.create(indexPath));
	}

	/**
	 * constructor parameters union
	 */
	public static <T> TypedIndexReaderTemplate<T> create(
			Class<T> tClass, int numHits, Path indexPath) {
		return new TypedIndexReaderTemplate<>(
				DocumentToTypedConverter.of(tClass),
				new DocumentIndexReaderTemplate(numHits, indexPath));
	}

	public <R> R transform(Function<Stream<T>, R> transformer) throws IOException {
		return docIndexReaderTemplate.transformDocuments(curry(this::doTransform, transformer));
	}

	public <R> R transform(Set<String> fieldNames, Function<Stream<T>, R> transformer) throws IOException {
		return docIndexReaderTemplate.transformDocuments(fieldNames, curry(this::doTransform, transformer));
	}

	public <R> R transformFieldValues(String fieldName, Function<Stream<String>, R> transformer) throws IOException {
		return docIndexReaderTemplate.transformFieldValues(fieldName, transformer::apply);
	}

	private <R> R doTransform(
			Function<Stream<T>, R> transformer, Stream<Document> documents) {
		return transformer.apply(documents.map(docToTypedConverter::convert).flatMap(Optional::stream));
	}
}
