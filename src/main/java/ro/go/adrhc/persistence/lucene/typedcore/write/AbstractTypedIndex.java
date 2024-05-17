package ro.go.adrhc.persistence.lucene.typedcore.write;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.core.write.DocsIndexWriter;
import ro.go.adrhc.persistence.lucene.typedcore.serde.TypedToDocumentConverter;
import ro.go.adrhc.util.Assert;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertStream;

@RequiredArgsConstructor
public abstract class AbstractTypedIndex<T> implements Closeable {
	protected final TypedToDocumentConverter<T> toDocumentConverter;
	protected final DocsIndexWriter docsIndexWriter;

	protected Stream<Document> toDocuments(Stream<T> tStream) {
		return convertStream(toDocumentConverter::convert, tStream);
	}

	protected Document toDocument(T t) {
		Optional<Document> documentOptional = toDocumentConverter.convert(t);
		Assert.isTrue(documentOptional.isPresent(), "Conversion failed!");
		return documentOptional.get();
	}

	@Override
	public void close() throws IOException {
		docsIndexWriter.close();
	}
}
