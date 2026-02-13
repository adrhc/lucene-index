package ro.go.adrhc.persistence.lucene.core.typed.write;

import org.apache.lucene.search.BooleanQuery;
import ro.go.adrhc.persistence.lucene.core.bare.write.DocsIndexWriter;
import ro.go.adrhc.persistence.lucene.core.typed.ExactQuery;
import ro.go.adrhc.persistence.lucene.core.typed.Identifiable;
import ro.go.adrhc.persistence.lucene.core.typed.serde.TypedToDocumentConverter;

import java.io.IOException;
import java.util.Collection;

import static ro.go.adrhc.persistence.lucene.core.bare.query.BooleanQueryFactory.shouldSatisfy;
import static ro.go.adrhc.persistence.lucene.core.typed.Identifiable.toIds;

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

	public void upsertMany(Collection<T> tCollection) throws IOException {
		BooleanQuery idsQuery = shouldSatisfy(exactQuery.newExactQueries(toIds(tCollection)));
		docsIndexWriter.upsertMany(idsQuery, toDocuments(tCollection));
	}
}
