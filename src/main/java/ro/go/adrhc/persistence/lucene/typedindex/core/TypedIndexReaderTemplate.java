package ro.go.adrhc.persistence.lucene.typedindex.core;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static ro.go.adrhc.util.fn.SneakyBiFunctionUtils.curry;

@RequiredArgsConstructor
public class TypedIndexReaderTemplate<T> {
	private final DocumentToTypedConverter<T> docToTypedConverter;
	private final DocumentIndexReaderTemplate docIndexReaderTemplate;

	public <R> R transformFileMetadata(Function<Stream<T>, R> transformer) throws IOException {
		return docIndexReaderTemplate.transformDocuments(curry(this::doTransform, transformer));
	}

	private <R> R doTransform(
			Function<Stream<T>, R> transformer, Stream<Document> documents) {
		return transformer.apply(documents.map(docToTypedConverter::convert).flatMap(Optional::stream));
	}
}
