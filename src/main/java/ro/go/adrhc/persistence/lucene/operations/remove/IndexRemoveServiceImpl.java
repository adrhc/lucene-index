package ro.go.adrhc.persistence.lucene.operations.remove;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemover;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemoverParams;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class IndexRemoveServiceImpl<I> implements IndexRemoveService<I> {
	private final TypedIndexRemover<I> typedIndexRemover;

	/**
	 * constructor parameters union
	 */
	public static <I> IndexRemoveServiceImpl<I>
	create(TypedIndexRemoverParams params) {
		return new IndexRemoveServiceImpl<>(TypedIndexRemover.create(params));
	}

	@Override
	public void removeById(I id) throws IOException {
		typedIndexRemover.removeOne(id);
	}

	@Override
	public void removeByIds(Collection<I> ids) throws IOException {
		typedIndexRemover.removeMany(ids);
	}

	@Override
	public void removeByQuery(Query query) throws IOException {
		typedIndexRemover.removeByQuery(query);
	}

	@Override
	public void removeAll() throws IOException {
		typedIndexRemover.removeAll();
	}
}
