package ro.go.adrhc.persistence.lucene.index;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.read.DocsIndexReader;
import ro.go.adrhc.persistence.lucene.core.read.DocsIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class DocsCountService implements IndexCountService {
    private final DocsIndexReaderTemplate docsReaderTemplate;

    /**
     * Query base DocsCountService
     * <p>
     * constructor parameters union
     * <p>
     * SearchedToQueryConverter = Optional::of
     */
    public static DocsCountService create(IndexReaderPool indexReaderPool) {
        return new DocsCountService(
                DocsIndexReaderTemplate.create(Integer.MAX_VALUE, indexReaderPool));
    }

    @Override
    public int count() throws IOException {
        return docsReaderTemplate.useReader(DocsIndexReader::count);
    }

    @Override
    public int count(Query query) throws IOException {
        return docsReaderTemplate.useReader(indexReader -> indexReader.count(query));
    }
}
