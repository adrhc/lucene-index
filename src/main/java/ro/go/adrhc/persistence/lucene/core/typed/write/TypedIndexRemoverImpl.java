package ro.go.adrhc.persistence.lucene.core.typed.write;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.bare.write.DocIndexWriter;
import ro.go.adrhc.persistence.lucene.core.typed.ExactQuery;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class TypedIndexRemoverImpl<I> implements TypedIndexRemover<I> {
	private final ExactQuery exactQuery;
	private final DocIndexWriter indexWriter;

	public static <I> TypedIndexRemoverImpl<I>
	create(TypedIndexRemoverParams params) {
		ExactQuery exactQuery = ExactQuery.create(params.idField());
		return new TypedIndexRemoverImpl<>(exactQuery, new DocIndexWriter(params.indexWriter()));
	}

	@Override
	public void removeOne(I id) throws IOException {
		indexWriter.deleteByQuery(exactQuery.newExactQuery(id));
	}

	@Override
	public void removeMany(Collection<? extends I> ids) throws IOException {
		indexWriter.deleteByQueries(exactQuery.newExactQueries(ids));
	}

	@Override
	public void removeByQuery(Query query) throws IOException {
		indexWriter.deleteByQuery(query);
	}

	@Override
	public void removeAll() throws IOException {
		indexWriter.deleteAll();
	}

	@Override
	public void commit() throws IOException {
		indexWriter.commit();
	}
}
