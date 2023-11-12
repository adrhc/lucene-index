package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.write.DocsIndexWriterTemplate;
import ro.go.adrhc.persistence.lucene.index.IndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedcore.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactoriesParams;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class TypedIndexRemoveService<ID> implements IndexRemoveService<ID> {
	private final ExactQuery exactQuery;
	private final DocsIndexWriterTemplate indexWriterTemplate;

	/**
	 * constructor parameters union
	 */
	public static <ID> TypedIndexRemoveService<ID>
	create(TypedIndexFactoriesParams<ID, ?, ?> factoriesParams) {
		ExactQuery exactQuery = ExactQuery.create(factoriesParams.getIdField());
		return new TypedIndexRemoveService<>(exactQuery,
				new DocsIndexWriterTemplate(factoriesParams.getIndexWriter()));
	}

	@Override
	public void removeByIds(Collection<ID> ids) throws IOException {
		indexWriterTemplate.useWriter(writer -> writer
				.deleteMany(exactQuery.newExactQueries(ids)));
	}

	@Override
	public void removeById(ID id) throws IOException {
		indexWriterTemplate.useWriter(writer -> writer
				.deleteOne(exactQuery.newExactQuery(id)));
	}
}
