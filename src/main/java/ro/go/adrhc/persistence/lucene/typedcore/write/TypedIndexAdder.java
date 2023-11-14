package ro.go.adrhc.persistence.lucene.typedcore.write;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.core.write.DocsIndexWriter;
import ro.go.adrhc.persistence.lucene.typedcore.serde.TypedToDocumentConverter;
import ro.go.adrhc.util.Assert;
import ro.go.adrhc.util.collection.StreamCounter;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static ro.go.adrhc.util.collection.StreamUtils.stream;
import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertStream;

@RequiredArgsConstructor
@Slf4j
public class TypedIndexAdder<T> implements Closeable {
	private final TypedToDocumentConverter<T> toDocumentConverter;
	private final DocsIndexWriter indexWriter;

	public static <T> TypedIndexAdder<T> create(TypedIndexAdderParams<T> params) {
		TypedToDocumentConverter<T> toDocumentConverter = TypedToDocumentConverter.create(params);
		return new TypedIndexAdder<>(toDocumentConverter, new DocsIndexWriter(params.getIndexWriter()));
	}

	public void addOne(T t) throws IOException {
		indexWriter.addOne(toDocument(t));
	}

	public void addMany(Iterable<? extends T> tIterable) throws IOException {
		addMany(stream(tIterable));
	}

	public void addMany(Stream<? extends T> tStream) throws IOException {
		StreamCounter tCounter = new StreamCounter();
		Stream<Document> documents = convertStream(
				toDocumentConverter::convert,
				tCounter.countedStream(tStream));
		StreamCounter dCounter = new StreamCounter();
		indexWriter.addMany(dCounter.countedStream(documents));
		if (tCounter.getCount() != dCounter.getCount()) {
			log.warn("Only {} of {} were successfully converted!",
					dCounter.getCount(), tCounter.getCount());
		}
	}

	protected Document toDocument(T t) {
		Optional<Document> documentOptional = toDocumentConverter.convert(t);
		Assert.isTrue(documentOptional.isPresent(), "Conversion failed!");
		return documentOptional.get();
	}

	@Override
	public void close() throws IOException {
		indexWriter.close();
	}
}
