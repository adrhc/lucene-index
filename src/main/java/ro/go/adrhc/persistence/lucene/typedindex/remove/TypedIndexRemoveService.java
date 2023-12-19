package ro.go.adrhc.persistence.lucene.typedindex.remove;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemoverParams;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemoverTemplate;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class TypedIndexRemoveService<ID> implements IndexRemoveService<ID> {
    private final TypedIndexRemoverTemplate<ID> indexRemoverTemplate;

    /**
     * constructor parameters union
     */
    public static <ID> TypedIndexRemoveService<ID>
    create(TypedIndexRemoverParams params) {
        return new TypedIndexRemoveService<>(TypedIndexRemoverTemplate.create(params));
    }

    @Override
    public void removeByIds(Collection<ID> ids) throws IOException {
        indexRemoverTemplate.useRemover(remover -> remover.removeMany(ids));
    }

    @Override
    public void removeById(ID id) throws IOException {
        indexRemoverTemplate.useRemover(remover -> remover.removeOne(id));
    }
}
