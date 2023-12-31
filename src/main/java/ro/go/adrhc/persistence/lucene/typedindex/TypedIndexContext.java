package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactoriesParams;
import ro.go.adrhc.persistence.lucene.typedindex.search.SearchResultFilter;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
@Slf4j
public class TypedIndexContext<T> implements Closeable, TypedIndexFactoriesParams<T> {
    private final Class<T> type;
    private final Collection<? extends TypedField<T>> typedFields;
    private final TypedField<T> idField;
    private final Analyzer analyzer;
    private final IndexWriter indexWriter;
    private final IndexReaderPool indexReaderPool;
    private final int numHits;
    private final SearchResultFilter<T> searchResultFilter;
    private final Path indexPath;

    @Override
    public void close() throws IOException {
        log.debug("\nclosing {} ...", indexPath);
        IOException exc = null;
        try {
            log.debug("\nclosing IndexReaderPool ...");
            indexReaderPool.close();
            log.debug("\nIndexReaderPool closed");
        } catch (IOException e) {
            exc = e;
        }
        try {
            log.info("\nclosing index ...");
            indexWriter.close();
            log.info("\n{} closed", indexPath);
        } catch (IOException e) {
            exc = e;
        }
        if (exc != null) {
            throw exc;
        }
    }

    public void commit() throws IOException {
        indexWriter.commit();
    }
}
