package ro.go.adrhc.persistence.lucene.typedindex.restore;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

public interface IndexDataSource<ID, T> {
    Stream<ID> loadAllIds() throws IOException;

    Stream<T> loadByIds(Stream<ID> ids) throws IOException;

    default Stream<T> loadAll() throws IOException {
        return loadByIds(loadAllIds());
    }

    default Stream<T> loadByIds(Collection<ID> ids) throws IOException {
        return loadByIds(ids.stream());
    }
}
