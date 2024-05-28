package ro.go.adrhc.persistence.lucene.typedindex;

import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

public interface IndexOperations<ID, T extends Identifiable<ID>>
		extends ReadOnlyIndexOperations<ID, T> {
	void addOne(T t) throws IOException;

	void addMany(Collection<T> tCollection) throws IOException;

	void addMany(Stream<T> tStream) throws IOException;

	void upsert(T t) throws IOException;

	void upsertAll(Iterable<T> iterable) throws IOException;

	void removeByIds(Collection<ID> ids) throws IOException;

	void removeById(ID id) throws IOException;

	void removeByQuery(Query query) throws IOException;

	/**
	 * remove the index content then set it to tIterable
	 */
	void reset(Iterable<T> tIterable) throws IOException;

	void reset(Stream<T> tStream) throws IOException;

	void restore(IndexDataSource<ID, T> dataSource) throws IOException;

	void restoreSubset(IndexDataSource<ID, T> dataSource, Query query) throws IOException;
}
