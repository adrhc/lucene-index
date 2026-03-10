package ro.go.adrhc.persistence.lucene.core.typed.write;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.core.bare.write.DocIndexWriter;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertCollection;
import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertStream;

@RequiredArgsConstructor
public abstract class AbstractIndexWriter<T> implements BasicIndexOperations {
	protected final TypedToDocumentConverter<T> toDocumentConverter;
	protected final DocIndexWriter docIndexWriter;

	@Override
	public void commit() throws IOException {
		docIndexWriter.commit();
	}

	protected Collection<Document> toDocuments(Collection<T> tCollection) {
		return convertCollection(toDocumentConverter::convert, tCollection);
	}

	protected Stream<Document> toDocuments(Stream<T> tStream) {
		return convertStream(toDocumentConverter::convert, tStream);
	}

	/**
	 * Throws IllegalStateException if the conversion fails!
	 */
	protected Document toDocument(T t) {
		return toDocumentConverter.convert(t).orElseThrow(
			() -> new IllegalStateException("Conversion to Lucene document failed!"));
	}
}
