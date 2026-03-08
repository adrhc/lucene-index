package ro.go.adrhc.persistence.lucene.core.typed.write;

import org.apache.lucene.search.BooleanQuery;
import ro.go.adrhc.persistence.lucene.core.bare.write.DocIndexWriter;
import ro.go.adrhc.persistence.lucene.core.typed.ExactQuery;
import ro.go.adrhc.persistence.lucene.core.typed.Identifiable;

import java.io.IOException;
import java.util.Collection;

import static ro.go.adrhc.persistence.lucene.core.bare.query.BooleanQueryFactory.shouldSatisfy;
import static ro.go.adrhc.persistence.lucene.core.typed.Identifiable.toIds;

public class TypedIndexUpsertImpl<T extends Identifiable<?>>
	extends AbstractIndexWriter<T> implements TypedIndexUpsert<T> {
	private final ExactQuery exactQuery;

	public TypedIndexUpsertImpl(TypedToDocumentConverter<T> toDocumentConverter,
		DocIndexWriter indexWriter, ExactQuery exactQuery) {
		super(toDocumentConverter, indexWriter);
		this.exactQuery = exactQuery;
	}

	public static <T extends Identifiable<?>>
	TypedIndexUpsert<T> create(TypedIndexUpsertParams<T> params) {
		return new TypedIndexUpsertImpl<>(
			TypedToDocumentConverter.create(params),
			new DocIndexWriter(params.indexWriter()),
			ExactQuery.create(params.idField()));
	}

	@Override
	public void upsert(T t) throws IOException {
		docIndexWriter.upsert(exactQuery.newExactQuery(t.getId()), toDocument(t));
	}

	@Override
	public void upsertMany(Collection<T> tCollection) throws IOException {
		BooleanQuery idsQuery = shouldSatisfy(exactQuery.newExactQueries(toIds(tCollection)));
		docIndexWriter.upsertMany(idsQuery, toDocuments(tCollection));
	}
}
