package ro.go.adrhc.persistence.lucene.typedcore.write;

import ro.go.adrhc.persistence.lucene.core.write.DocsIndexWriter;
import ro.go.adrhc.persistence.lucene.typedcore.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedcore.serde.TypedToDocumentConverter;

import java.io.IOException;

public class TypedIndexUpdater<T extends Identifiable<?>> extends AbstractTypedIndex<T> {
	private final ExactQuery exactQuery;

	public TypedIndexUpdater(TypedToDocumentConverter<T> toDocumentConverter,
			DocsIndexWriter indexWriter, ExactQuery exactQuery) {
		super(toDocumentConverter, indexWriter);
		this.exactQuery = exactQuery;
	}

	public static <T extends Identifiable<?>>
	TypedIndexUpdater<T> create(TypedIndexUpdaterParams<T> params) {
		return new TypedIndexUpdater<>(
				TypedToDocumentConverter.create(params),
				new DocsIndexWriter(params.getIndexWriter()),
				ExactQuery.create(params.getIdField()));
	}

	public void update(T t) throws IOException {
		docsIndexWriter.update(exactQuery.newExactQuery(t.getId()), toDocument(t));
	}
}
