package ro.go.adrhc.persistence.lucene.typedindex.search;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.search.DocumentsSearchByIdService;
import ro.go.adrhc.persistence.lucene.index.search.SearchByIdService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexContext;
import ro.go.adrhc.persistence.lucene.typedindex.domain.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.docserde.DocumentToTypedConverter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class TypedSearchByIdService<ID, T extends Identifiable<ID>> implements SearchByIdService<ID, T> {
	private final DocumentToTypedConverter<T> documentToTypedConverter;
	private final SearchByIdService<ID, Document> searchByIdService;

	public static <ID, T extends Identifiable<ID>>
	TypedSearchByIdService<ID, T> create(TypedIndexContext<ID, T, ?> typedIndexContext) {
		return new TypedSearchByIdService<>(
				DocumentToTypedConverter.of(typedIndexContext.getType()),
				DocumentsSearchByIdService.create(
						typedIndexContext.getIdField(), typedIndexContext.getIndexReaderPool()));
	}

	public Optional<T> findById(ID id) throws IOException {
		return searchByIdService.findById(id)
				.flatMap(documentToTypedConverter::convert);
	}
}
