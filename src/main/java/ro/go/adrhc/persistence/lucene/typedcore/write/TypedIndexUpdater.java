package ro.go.adrhc.persistence.lucene.typedcore.write;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.core.write.DocsIndexWriter;
import ro.go.adrhc.persistence.lucene.typedcore.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedcore.serde.TypedToDocumentConverter;
import ro.go.adrhc.util.Assert;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class TypedIndexUpdater<T extends Identifiable<?>> implements Closeable {
	private final TypedToDocumentConverter<T> toDocumentConverter;
	private final ExactQuery exactQuery;
	private final DocsIndexWriter indexWriter;

	public static <T extends Identifiable<?>>
	TypedIndexUpdater<T> create(TypedIndexUpdaterParams<T> params) {
		TypedToDocumentConverter<T> toDocumentConverter = TypedToDocumentConverter.create(params);
		ExactQuery exactQuery = ExactQuery.create(params.getIdField());
		return new TypedIndexUpdater<>(toDocumentConverter,
				exactQuery, new DocsIndexWriter(params.getIndexWriter()));
	}

	public void update(T t) throws IOException {
		indexWriter.update(exactQuery.newExactQuery(t.getId()), toDocument(t));
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
