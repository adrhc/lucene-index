package ro.go.adrhc.persistence.lucene.typedindex.reset;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.go.adrhc.persistence.lucene.typedcore.write.AbstractTypedIndexParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexResetTemplate;

import java.io.IOException;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class TypedIndexResetService<T> implements IndexResetService<T> {
    private final TypedIndexResetTemplate<T> indexResetTemplate;

    /**
     * constructor parameters union
     */
    public static <T> TypedIndexResetService<T> create(AbstractTypedIndexParams<T> params) {
        return new TypedIndexResetService<>(TypedIndexResetTemplate.create(params));
    }

    public void reset(Stream<T> tStream) throws IOException {
        indexResetTemplate.useReset(resetWriter -> resetWriter.reset(tStream));
    }
}
