package ro.go.adrhc.persistence.lucene.typedindex.search;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.SearchByIdService;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.docserde.DocumentToTypedConverter;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@RequiredArgsConstructor
public class TypedSearchByIdService<ID, T extends Identifiable<ID>> {
	private final DocumentToTypedConverter<T> documentToTypedConverter;
	private final SearchByIdService<ID> searchByIdService;

	public static <ID, T extends Identifiable<ID>>
	TypedSearchByIdService<ID, T> create(
			Class<T> tClass, TypedField<?> idField, Path indexPath) {
		return new TypedSearchByIdService<>(
				DocumentToTypedConverter.of(tClass),
				SearchByIdService.create(idField, indexPath));
	}

	public Optional<T> findById(ID id) throws IOException {
		return searchByIdService.findById(id)
				.flatMap(documentToTypedConverter::convert);
	}
}