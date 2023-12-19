package ro.go.adrhc.persistence.lucene.typedcore.write;

import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;

import java.io.IOException;

@RequiredArgsConstructor
public class TypedIndexUpdaterTemplate<T extends Identifiable<?>> {
    private final TypedIndexUpdater<T> indexUpdater;

    public static <T extends Identifiable<?>>
    TypedIndexUpdaterTemplate<T> create(TypedIndexUpdaterParams<T> params) {
        TypedIndexUpdater<T> indexUpdater = TypedIndexUpdater.create(params);
        return new TypedIndexUpdaterTemplate<>(indexUpdater);
    }

    public <E extends Exception> void useUpdater(
            SneakyConsumer<TypedIndexUpdater<T>, E> indexUpdaterConsumer)
            throws IOException, E {
        try (TypedIndexUpdater<T> indexUpdater = this.indexUpdater) {
            indexUpdaterConsumer.accept(indexUpdater);
        }
    }
}
