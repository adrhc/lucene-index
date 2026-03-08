package ro.go.adrhc.persistence.lucene.core.typed.write;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.core.bare.write.DocIndexWriter;
import ro.go.adrhc.util.stream.StreamCounter;

import java.io.IOException;
import java.util.stream.Stream;

import static ro.go.adrhc.util.stream.StreamUtils.stream;

@Slf4j
public class TypedIndexAdder<T> extends AbstractIndexWriter<T> {
	public TypedIndexAdder(TypedToDocumentConverter<T> toDocumentConverter,
		DocIndexWriter indexWriter) {
		super(toDocumentConverter, indexWriter);
	}

	public static <T> TypedIndexAdder<T> create(TypedIndexWriterParams<T> params) {
		TypedToDocumentConverter<T> toDocumentConverter =
			TypedToDocumentConverter.create(params);
		return new TypedIndexAdder<>(toDocumentConverter,
			new DocIndexWriter(params.indexWriter()));
	}

	public void addOne(T t) throws IOException {
		docIndexWriter.addOne(toDocument(t));
	}

	public void addMany(Iterable<T> tIterable) throws IOException {
		addMany(stream(tIterable));
	}

	public void addMany(Stream<T> tStream) throws IOException {
		StreamCounter tCounter = new StreamCounter();
		Stream<Document> documents = toDocuments(tCounter.countedStream(tStream));
		StreamCounter dCounter = new StreamCounter();
		docIndexWriter.addMany(dCounter.countedStream(documents));
		if (tCounter.getCount() != dCounter.getCount()) {
			log.warn("Only {} of {} were successfully converted!",
				dCounter.getCount(), tCounter.getCount());
		}
	}
}
