package ro.go.adrhc.persistence.lucene.typedcore.read;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.read.DocsIndexReader;
import ro.go.adrhc.persistence.lucene.core.read.ScoreAndDocument;
import ro.go.adrhc.persistence.lucene.typedcore.serde.DocumentToTypedConverter;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class OneHitIndexReader<T> implements Closeable {
    private final DocumentToTypedConverter<T> docToTypedConverter;
    private final DocsIndexReader indexReader;

    public static <T> OneHitIndexReader<T> create(OneHitIndexReaderParams<T> params) throws IOException {
        DocumentToTypedConverter<T> docToTypedConverter = DocumentToTypedConverter.create(params.getType());
        return new OneHitIndexReader<>(docToTypedConverter,
                DocsIndexReader.create(1, params.getIndexReaderPool()));
    }

    public Optional<T> findFirst(Query query) throws IOException {
        return indexReader.findMany(query)
                .map(ScoreAndDocument::document)
                .findAny() // DocsIndexReader is created with numHits = 1
                .flatMap(docToTypedConverter::convert);
    }

    @Override
    public void close() throws IOException {
        indexReader.close();
    }
}
