package ro.go.adrhc.persistence.lucene.index.operations.remove;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemoverTemplate;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class IndexRemoveServiceImpl<ID> implements IndexRemoveService<ID> {
	private final TypedIndexRemoverTemplate<ID> indexRemoverTemplate;

	/**
	 * constructor parameters union
	 */
	public static <ID> IndexRemoveServiceImpl<ID>
	create(TypedIndexRemoverParams params) {
		return new IndexRemoveServiceImpl<>(TypedIndexRemoverTemplate.create(params));
	}

	@Override
	public void removeByIds(Collection<ID> ids) throws IOException {
		indexRemoverTemplate.useRemover(remover -> remover.removeMany(ids));
	}

	@Override
	public void removeById(ID id) throws IOException {
		indexRemoverTemplate.useRemover(remover -> remover.removeOne(id));
	}

	@Override
	public void removeByQuery(Query query) throws IOException {
		indexRemoverTemplate.useRemover(remover -> remover.removeByQuery(query));
	}
}
