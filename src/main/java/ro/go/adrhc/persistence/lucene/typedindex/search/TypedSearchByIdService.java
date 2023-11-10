package ro.go.adrhc.persistence.lucene.typedindex.search;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.search.DocumentsSearchByIdService;
import ro.go.adrhc.persistence.lucene.index.search.SearchByIdService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexSpec;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.docserde.DocumentToTypedConverter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class TypedSearchByIdService<ID, T extends Identifiable<ID>> implements SearchByIdService<ID, T> {
	private final DocumentToTypedConverter<T> documentToTypedConverter;
	private final SearchByIdService<ID, Document> searchByIdService;

	public static <ID, T extends Identifiable<ID>>
	TypedSearchByIdService<ID, T> create(TypedIndexSpec<ID, T, ?> typedIndexSpec) {
		return new TypedSearchByIdService<>(
				DocumentToTypedConverter.of(typedIndexSpec.getType()),
				DocumentsSearchByIdService.create(
						typedIndexSpec.getIdField(), typedIndexSpec.getIndexReaderPool()));
	}

	public Optional<T> findById(ID id) throws IOException {
		return searchByIdService.findById(id)
				.flatMap(documentToTypedConverter::convert);
	}
}
