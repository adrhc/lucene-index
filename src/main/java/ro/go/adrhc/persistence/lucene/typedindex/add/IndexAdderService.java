package ro.go.adrhc.persistence.lucene.typedindex.add;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

public interface IndexAdderService<T> {
    void addOne(T t) throws IOException;

    void addMany(Collection<T> tCollection) throws IOException;

    void addMany(Stream<T> tStream) throws IOException;
}
