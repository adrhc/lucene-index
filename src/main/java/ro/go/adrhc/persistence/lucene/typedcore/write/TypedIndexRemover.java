package ro.go.adrhc.persistence.lucene.typedcore.write;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.write.DocsIndexWriter;
import ro.go.adrhc.persistence.lucene.typedcore.ExactQuery;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class TypedIndexRemover<ID> implements Closeable {
	private final ExactQuery exactQuery;
	private final DocsIndexWriter indexWriter;

	public static <ID> TypedIndexRemover<ID>
	create(TypedIndexRemoverParams params) {
		ExactQuery exactQuery = ExactQuery.create(params.getIdField());
		return new TypedIndexRemover<>(exactQuery, new DocsIndexWriter(params.getIndexWriter()));
	}

	public void removeOne(ID id) throws IOException {
		indexWriter.deleteByQuery(exactQuery.newExactQuery(id));
	}

	public void removeMany(Collection<? extends ID> ids) throws IOException {
		indexWriter.deleteByQueries(exactQuery.newExactQueries(ids));
	}

	public void removeByQuery(Query query) throws IOException {
		indexWriter.deleteByQuery(query);
	}

	public void removeAll() throws IOException {
		indexWriter.deleteAll();
	}

	@Override
	public void close() throws IOException {
		indexWriter.close();
	}
}
