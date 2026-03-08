package ro.go.adrhc.persistence.lucene.core.typed.write;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.bare.write.DocIndexWriter;
import ro.go.adrhc.persistence.lucene.core.typed.ExactQuery;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class TypedIndexRemover<I> {
	private final ExactQuery exactQuery;
	private final DocIndexWriter indexWriter;

	public static <I> TypedIndexRemover<I>
	create(TypedIndexRemoverParams params) {
		ExactQuery exactQuery = ExactQuery.create(params.idField());
		return new TypedIndexRemover<>(exactQuery, new DocIndexWriter(params.indexWriter()));
	}

	public void removeOne(I id) throws IOException {
		indexWriter.deleteByQuery(exactQuery.newExactQuery(id));
	}

	public void removeMany(Collection<? extends I> ids) throws IOException {
		indexWriter.deleteByQueries(exactQuery.newExactQueries(ids));
	}

	public void removeByQuery(Query query) throws IOException {
		indexWriter.deleteByQuery(query);
	}

	public void removeAll() throws IOException {
		indexWriter.deleteAll();
	}
}
