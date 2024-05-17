package ro.go.adrhc.persistence.lucene.typedcore.write;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.core.write.DocsIndexWriter;
import ro.go.adrhc.persistence.lucene.typedcore.serde.TypedToDocumentConverter;
import ro.go.adrhc.util.stream.StreamCounter;

import java.io.IOException;
import java.util.stream.Stream;

import static ro.go.adrhc.util.stream.StreamUtils.stream;

@Slf4j
public class TypedIndexAdder<T> extends AbstractTypedIndex<T> {
	public TypedIndexAdder(TypedToDocumentConverter<T> toDocumentConverter, DocsIndexWriter indexWriter) {
		super(toDocumentConverter, indexWriter);
	}

	public static <T> TypedIndexAdder<T> create(AbstractTypedIndexParams<T> params) {
		TypedToDocumentConverter<T> toDocumentConverter = TypedToDocumentConverter.create(params);
		return new TypedIndexAdder<>(toDocumentConverter, new DocsIndexWriter(params.getIndexWriter()));
	}

	public void addOne(T t) throws IOException {
		docsIndexWriter.addOne(toDocument(t));
	}

	public void addMany(Iterable<T> tIterable) throws IOException {
		addMany(stream(tIterable));
	}

	public void addMany(Stream<T> tStream) throws IOException {
		StreamCounter tCounter = new StreamCounter();
		Stream<Document> documents = toDocuments(tCounter.countedStream(tStream));
		StreamCounter dCounter = new StreamCounter();
		docsIndexWriter.addMany(dCounter.countedStream(documents));
		if (tCounter.getCount() != dCounter.getCount()) {
			log.warn("Only {} of {} were successfully converted!",
					dCounter.getCount(), tCounter.getCount());
		}
	}
}
