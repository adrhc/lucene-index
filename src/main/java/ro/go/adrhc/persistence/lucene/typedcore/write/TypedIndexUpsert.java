package ro.go.adrhc.persistence.lucene.typedcore.write;

import ro.go.adrhc.persistence.lucene.core.write.DocsIndexWriter;
import ro.go.adrhc.persistence.lucene.typedcore.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedcore.Identifiable;
import ro.go.adrhc.persistence.lucene.typedcore.serde.TypedToDocumentConverter;

import java.io.IOException;

public class TypedIndexUpsert<T extends Identifiable<?>> extends AbstractTypedIndexWriter<T> {
	private final ExactQuery exactQuery;

	public TypedIndexUpsert(TypedToDocumentConverter<T> toDocumentConverter,
			DocsIndexWriter indexWriter, ExactQuery exactQuery) {
		super(toDocumentConverter, indexWriter);
		this.exactQuery = exactQuery;
	}

	public static <T extends Identifiable<?>>
	TypedIndexUpsert<T> create(TypedIndexUpsertParams<T> params) {
		return new TypedIndexUpsert<>(
				TypedToDocumentConverter.create(params),
				new DocsIndexWriter(params.getIndexWriter()),
				ExactQuery.create(params.getIdField()));
	}

	public void upsert(T t) throws IOException {
		docsIndexWriter.upsert(exactQuery.newExactQuery(t.getId()), toDocument(t));
	}
}
